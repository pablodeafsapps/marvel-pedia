package es.plexus.android.marvelpedia.datalayer.repository

import arrow.core.Either
import arrow.core.left
import es.plexus.android.marvelpedia.datalayer.datasource.AuthenticationDataSource
import es.plexus.android.marvelpedia.datalayer.datasource.ConnectivityDataSource
import es.plexus.android.marvelpedia.datalayer.datasource.JokesDataSource
import es.plexus.android.marvelpedia.datalayer.domain.FailureDto
import es.plexus.android.marvelpedia.datalayer.domain.boToDto
import es.plexus.android.marvelpedia.datalayer.domain.dtoToBoFailure
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo
import es.plexus.android.marvelpedia.domainlayer.domain.JokeBoWrapper
import es.plexus.android.marvelpedia.domainlayer.domain.UserLoginBo
import java.lang.Exception

object Repository : DomainlayerContract.Datalayer.AuthenticationRepository<UserLoginBo, Boolean>,
    DomainlayerContract.Datalayer.DataRepository<JokeBoWrapper> {

    lateinit var connectivityDataSource: ConnectivityDataSource
    lateinit var authenticationDataSource: AuthenticationDataSource
    lateinit var jokesDataSource: JokesDataSource

    /**
     * This method logs in a user using some personal-info parameters. It first checks whether a
     * connection is available, and if so queries the data-source.
     *
     * @param [UserLoginBo] containing the mandatory data for this request
     * @return A [Boolean] value illustrating the outcome, or an error otherwise
     */
    override suspend fun loginUser(params: UserLoginBo): Either<FailureBo, Boolean> =
        try {
            connectivityDataSource.checkNetworkConnectionAvailability().takeIf { it }?.let {
                authenticationDataSource.requestLogin(userData = params.boToDto())
                    .mapLeft { FailureDto.FirebaseLoginError.dtoToBoFailure() }
            } ?: run {
                FailureDto.NoConnection.dtoToBoFailure().left()
            }
        } catch (e: Exception) {
            println("requestLogin(...) - Error: ${e.message}")
            FailureDto.Unknown.dtoToBoFailure().left()
        }

    /**
     * This method registers a user using some personal-info parameters. It first checks whether a
     * connection is available, and if so queries the data-source.
     *
     * @param [UserLoginBo] containing the mandatory data for this request
     * @return A [Boolean] value illustrating the outcome, or an error otherwise
     */
    override suspend fun registerUser(params: UserLoginBo): Either<FailureBo, Boolean> =
        try {
            connectivityDataSource.checkNetworkConnectionAvailability().takeIf { it }?.let {
                authenticationDataSource.requestRegister(userData = params.boToDto())
                    .mapLeft { FailureDto.FirebaseRegisterError(msg = it.msg).dtoToBoFailure() }
            } ?: run {
                FailureDto.NoConnection.dtoToBoFailure().left()
            }
        } catch (e: Exception) {
            println("requestRegister(...) - Error: ${e.message}")
            FailureDto.Unknown.dtoToBoFailure().left()
        }

    /**
     * This method fetches a list of jokes by querying the corresponding data-source.
     *
     * @return A [JokeBoWrapper] or an error otherwise
     */
    override suspend fun fetchJokes(): Either<FailureBo, JokeBoWrapper> =
        jokesDataSource.fetchJokesResponse()
}

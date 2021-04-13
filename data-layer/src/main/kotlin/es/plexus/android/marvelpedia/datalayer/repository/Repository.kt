package es.plexus.android.marvelpedia.datalayer.repository

import arrow.core.Either
import arrow.core.left
import es.plexus.android.marvelpedia.datalayer.datasource.CharactersDataSource
import es.plexus.android.marvelpedia.datalayer.datasource.ConnectivityDataSource
import es.plexus.android.marvelpedia.datalayer.domain.FailureDto
import es.plexus.android.marvelpedia.datalayer.domain.dtoToBoFailure
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo

object Repository : DomainlayerContract.Datalayer.DataRepository<CharacterDataBoWrapper> {

    lateinit var connectivityDataSource: ConnectivityDataSource
    lateinit var charactersDataSource: CharactersDataSource

    /**
     * This method fetches a list of characters by querying the corresponding data-source.
     *
     * @return A [CharacterDataBoWrapper] or an error otherwise
     */
    override suspend fun fetchCharacters(): Either<FailureBo, CharacterDataBoWrapper> =
        try {
            connectivityDataSource.checkNetworkConnectionAvailability().takeIf { it }?.let {
                charactersDataSource.fetchCharactersResponse()
            } ?: run {
                FailureBo.NoConnection.left()
            }
        } catch (e: Exception) {
            println("requestLogin(...) - Error: ${e.message}")
            FailureDto.Unknown.dtoToBoFailure().left()
        }

    /**
     * This method fetches a single character, according to an identifier, by querying the
     * corresponding data-source.
     *
     * @return A [CharacterDataBoWrapper] or an error otherwise
     */
    override suspend fun fetchCharacterDetailsByIdUc(id: Int): Either<FailureBo, CharacterDataBoWrapper> =
        try {
            connectivityDataSource.checkNetworkConnectionAvailability().takeIf { it }?.let {
                charactersDataSource.fetchCharacterDetailsByIdResponse(id = id.toString())
            } ?: run {
                FailureBo.NoConnection.left()
            }
        } catch (e: Exception) {
            println("requestLogin(...) - Error: ${e.message}")
            FailureDto.Unknown.dtoToBoFailure().left()
        }

}

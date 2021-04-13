package es.plexus.android.marvelpedia.datalayer.repository

import arrow.core.Either
import arrow.core.left
import es.plexus.android.marvelpedia.datalayer.datasource.ConnectivityDataSource
import es.plexus.android.marvelpedia.datalayer.datasource.MoviesDataSource
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo

object Repository : DomainlayerContract.Datalayer.DataRepository<CharacterDataBoWrapper> {

    lateinit var connectivityDataSource: ConnectivityDataSource
    lateinit var moviesDataSource: MoviesDataSource

    /**
     * This method fetches a list of characters by querying the corresponding data-source.
     *
     * @return A [CharacterDataBoWrapper] or an error otherwise
     */
    override suspend fun fetchCharacters(): Either<FailureBo, CharacterDataBoWrapper> =
        connectivityDataSource.checkNetworkConnectionAvailability().takeIf { it }?.let {
            moviesDataSource.fetchCharactersResponse()
        } ?: run {
            FailureBo.NoConnection.left()
        }

}

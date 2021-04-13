package es.plexus.android.marvelpedia.domainlayer.usecase

import arrow.core.Either
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo

const val FETCH_CHARACTERS_UC_TAG = "fetchCharactersUc"

/**
 * A use-case which allows to fetch a list of movies from a repository
 *
 * @property [dataRepository] The repository responsible of bringing the required data
 */
class FetchCharactersUc(private val dataRepository: DomainlayerContract.Datalayer.DataRepository<String>) :
    DomainlayerContract.Presentationlayer.UseCase<Any, String> {

    override suspend fun run(params: Any?): Either<FailureBo, String> = dataRepository.fetchCharacters()

}

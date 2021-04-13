package es.plexus.android.marvelpedia.domainlayer.usecase

import arrow.core.Either
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo

const val FETCH_CHARACTERS_UC_TAG = "fetchCharactersUc"

/**
 * A use-case which allows to fetch a list of characters from a repository
 *
 * @property [dataRepository] The repository responsible of bringing the required data
 */
class FetchCharactersUc(
    private val dataRepository: DomainlayerContract.Datalayer.DataRepository<CharacterDataBoWrapper>
) : DomainlayerContract.Presentationlayer.UseCase<Any, CharacterDataBoWrapper> {

    override suspend fun run(params: Any?): Either<FailureBo, CharacterDataBoWrapper> =
        dataRepository.fetchCharacters()

}

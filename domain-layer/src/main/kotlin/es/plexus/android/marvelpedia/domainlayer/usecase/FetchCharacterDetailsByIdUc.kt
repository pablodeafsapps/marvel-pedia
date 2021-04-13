package es.plexus.android.marvelpedia.domainlayer.usecase

import arrow.core.Either
import arrow.core.left
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterBo
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.domain.ErrorMessage
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo

const val FETCH_CHARACTER_DETAILS_BY_ID_UC_TAG = "fetchCharacterDetailsByIdUc"
private const val SINGLE_VALUE = 0

/**
 * A use-case which allows to fetch a single character data from a repository
 *
 * @property [dataRepository] The repository responsible of bringing the required data
 */
class FetchCharacterDetailsByIdUc(
    private val dataRepository: DomainlayerContract.Datalayer.DataRepository<CharacterDataBoWrapper>
) : DomainlayerContract.Presentationlayer.UseCase<CharacterBo, CharacterBo> {

    override suspend fun run(params: CharacterBo?): Either<FailureBo, CharacterBo> =
        params?.let { p ->
            dataRepository.fetchCharacterDetailsByIdUc(id = p.id).map { it.data.results[SINGLE_VALUE] }
        } ?: run {
            FailureBo.InputParamsError(msg = ErrorMessage.ERROR_PARAMS_CANNOT_BE_EMPTY).left()
        }

}

package es.plexus.android.marvelpedia.domainlayer.feature.detail

import arrow.core.Either
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.base.BaseDomainLayerBridge
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterBo
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo
import kotlinx.coroutines.CoroutineScope

const val DETAIL_DOMAIN_BRIDGE_TAG = "detailDomainLayerBridge"

/**
 * An entity which gathers all the available functionality related to the 'main' feature
 */
interface DetailDomainLayerBridge<out S> : BaseDomainLayerBridge {

    /**
     * A function which allows to query and handle joke data
     *
     * @param [scope] The [CoroutineScope] associated, which can be used to handle an enclosing Kotlin Coroutine
     * @param [onResult] A callback to send back any data of interest
     */
    fun fetchCharacterDetailsById(
        scope: CoroutineScope,
        params: CharacterBo?,
        onResult: (Either<FailureBo, S>) -> Unit = {}
    )

}

internal class DetailDomainLayerBridgeImpl(
    private val fetchCharacterDetailsByIdUc: DomainlayerContract.Presentationlayer.UseCase<CharacterBo, CharacterBo>
) : DetailDomainLayerBridge<CharacterBo> {

    override fun fetchCharacterDetailsById(
        scope: CoroutineScope,
        params: CharacterBo?,
        onResult: (Either<FailureBo, CharacterBo>) -> Unit
    ) {
        fetchCharacterDetailsByIdUc.invoke(scope = scope, params = params, onResult = onResult)
    }

}

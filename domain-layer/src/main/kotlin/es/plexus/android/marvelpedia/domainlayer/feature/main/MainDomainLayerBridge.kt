package es.plexus.android.marvelpedia.domainlayer.feature.main

import arrow.core.Either
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.base.BaseDomainLayerBridge
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo
import kotlinx.coroutines.CoroutineScope

const val MAIN_DOMAIN_BRIDGE_TAG = "mainDomainLayerBridge"

/**
 * An entity which gathers all the available functionality related to the 'main' feature
 */
interface MainDomainLayerBridge<out S> : BaseDomainLayerBridge {

    /**
     * A function which allows to query and handle joke data
     *
     * @param [scope] The [CoroutineScope] associated, which can be used to handle an enclosing Kotlin Coroutine
     * @param [onResult] A callback to send back any data of interest
     */
    fun fetchJokes(scope: CoroutineScope, onResult: (Either<FailureBo, S>) -> Unit = {})

}

internal class MainDomainLayerBridgeImpl(
    private val fetchJokesUc: DomainlayerContract.Presentationlayer.UseCase<Any, String>
) : MainDomainLayerBridge<String> {

    override fun fetchJokes(
        scope: CoroutineScope,
        onResult: (Either<FailureBo, String>) -> Unit
    ) {
        fetchJokesUc.invoke(scope = scope, onResult = onResult)
    }

}

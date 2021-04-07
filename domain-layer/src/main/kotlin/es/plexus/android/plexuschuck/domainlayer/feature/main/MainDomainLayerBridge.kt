package es.plexus.android.plexuschuck.domainlayer.feature.main

import arrow.core.Either
import es.plexus.android.plexuschuck.domainlayer.DomainlayerContract
import es.plexus.android.plexuschuck.domainlayer.base.BaseDomainLayerBridge
import es.plexus.android.plexuschuck.domainlayer.domain.FailureBo
import es.plexus.android.plexuschuck.domainlayer.domain.JokeBoWrapper
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
    private val fetchJokesUc: DomainlayerContract.Presentationlayer.UseCase<Any, JokeBoWrapper>
) : MainDomainLayerBridge<JokeBoWrapper> {

    override fun fetchJokes(
        scope: CoroutineScope,
        onResult: (Either<FailureBo, JokeBoWrapper>) -> Unit
    ) {
        fetchJokesUc.invoke(scope = scope, onResult = onResult)
    }

}

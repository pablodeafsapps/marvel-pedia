package es.plexus.android.marvelpedia.presentationlayer.base

import es.plexus.android.marvelpedia.domainlayer.base.BaseDomainLayerBridge
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * This parametrized interface is intended to be implemented by any app presentation-layer view which aims to be
 * integrated within the MVVM architecture pattern.
 *
 * @param T represents the ViewModel, and thus it must extend from @see{BaseMvvmViewModel}
 * @param S represents the bridge to the domain layer, and must extend from @see{BaseDomainLayerBridge}
 * @param U represents the state of the view, and must extend from @see{BaseState}
 * @property viewModel a reference to the [BaseMvvmViewModel] associated to this view
 *
 * @since 1.0
 */
@ExperimentalCoroutinesApi
interface BaseMvvmView<T : BaseMvvmViewModel<S, U>, S : BaseDomainLayerBridge, U : BaseState> {

    val viewModel: T

    /**
     * Handles the possible state values
     *
     * @param renderState the actual state, extending from U
     */
    fun processRenderState(renderState: U)

    /**
     * Init viewModel
     */
    fun initModel()

}

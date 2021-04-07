package es.plexus.android.plexuschuck.presentationlayer.feature.detail.viewmodel

import es.plexus.android.plexuschuck.domainlayer.base.BaseDomainLayerBridge
import es.plexus.android.plexuschuck.presentationlayer.base.BaseMvvmViewModel
import es.plexus.android.plexuschuck.presentationlayer.base.ScreenState
import es.plexus.android.plexuschuck.presentationlayer.domain.FailureVo
import es.plexus.android.plexuschuck.presentationlayer.domain.JokeVo
import es.plexus.android.plexuschuck.presentationlayer.feature.detail.view.state.DetailState
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * This [BaseMvvmViewModel] handles the 'detail' feature view-model. Therefore, it is in charge of
 * publishing joke detail information to all subscribers.
 *
 * All results update an observable variable, [_screenState], with [DetailState] values.
 */
@ExperimentalCoroutinesApi
class DetailViewModel(bridge: BaseDomainLayerBridge.None) :
    BaseMvvmViewModel<BaseDomainLayerBridge.None, DetailState>(bridge = bridge) {

    /**
     * Indicates that the associated view has been created
     */
    fun onViewCreated(jokeItem: JokeVo?) {
        _screenState.value = ScreenState.Render(
            if (jokeItem != null) DetailState.ShowJokeInfo(joke = jokeItem)
            else DetailState.ShowError(FailureVo.NoData)
        )
    }

}

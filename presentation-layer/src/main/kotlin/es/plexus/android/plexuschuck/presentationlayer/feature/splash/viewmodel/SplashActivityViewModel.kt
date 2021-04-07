package es.plexus.android.plexuschuck.presentationlayer.feature.splash.viewmodel

import es.plexus.android.plexuschuck.domainlayer.base.BaseDomainLayerBridge
import es.plexus.android.plexuschuck.presentationlayer.base.BaseMvvmViewModel
import es.plexus.android.plexuschuck.presentationlayer.base.ScreenState
import es.plexus.android.plexuschuck.presentationlayer.feature.splash.view.state.SplashState
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * This [BaseMvvmViewModel] handles the 'splash' feature view-model. Therefore, it is in charge of
 * the logic which makes the app loads all the necessary data for a proper performance. It uses no
 * bridge whatsoever.
 *
 * All results update an observable variable, [_screenState], with [SplashState] values.
 */
@ExperimentalCoroutinesApi
class SplashActivityViewModel(bridge: BaseDomainLayerBridge.None) :
    BaseMvvmViewModel<BaseDomainLayerBridge.None, SplashState>(bridge = bridge) {

    /**
     * Indicates that the associated view has been created
     */
    fun onViewCreated() {
        _screenState.value = ScreenState.Render(SplashState.LoadingFinished)
    }

}

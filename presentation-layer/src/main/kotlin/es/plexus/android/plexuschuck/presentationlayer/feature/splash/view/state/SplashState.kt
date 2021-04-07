package es.plexus.android.plexuschuck.presentationlayer.feature.splash.view.state

import es.plexus.android.plexuschuck.presentationlayer.base.BaseState

/**
 * Models all available states for the 'Splash' feature view
 */
sealed class SplashState : BaseState() {
    object LoadingFinished : SplashState()
}

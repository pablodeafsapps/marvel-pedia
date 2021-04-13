package es.plexus.android.marvelpedia.presentationlayer.feature.splash.view.state

import es.plexus.android.marvelpedia.presentationlayer.base.BaseState

/**
 * Models all available states for the 'Splash' feature view
 */
sealed class SplashState : BaseState() {
    object LoadingFinished : SplashState()
}

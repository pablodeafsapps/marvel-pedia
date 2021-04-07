package es.plexus.android.plexuschuck.presentationlayer.feature.login.view.state

import es.plexus.android.plexuschuck.presentationlayer.base.BaseState
import es.plexus.android.plexuschuck.presentationlayer.domain.FailureVo

/**
 * Models all available states for the 'Login' feature view
 */
sealed class LoginState : BaseState() {
    object Login : LoginState()
    object Register : LoginState()
    object AccessGranted : LoginState()
    class ShowError(val failure: FailureVo) : LoginState()
}

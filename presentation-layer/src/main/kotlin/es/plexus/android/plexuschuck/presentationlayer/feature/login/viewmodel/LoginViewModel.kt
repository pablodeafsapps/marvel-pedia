package es.plexus.android.plexuschuck.presentationlayer.feature.login.viewmodel

import androidx.lifecycle.viewModelScope
import es.plexus.android.plexuschuck.domainlayer.domain.FailureBo
import es.plexus.android.plexuschuck.domainlayer.domain.UserLoginBo
import es.plexus.android.plexuschuck.domainlayer.feature.login.LoginDomainLayerBridge
import es.plexus.android.plexuschuck.presentationlayer.base.BaseMvvmViewModel
import es.plexus.android.plexuschuck.presentationlayer.base.ScreenState
import es.plexus.android.plexuschuck.presentationlayer.domain.FailureVo
import es.plexus.android.plexuschuck.presentationlayer.domain.UserLoginVo
import es.plexus.android.plexuschuck.presentationlayer.domain.boToVoFailure
import es.plexus.android.plexuschuck.presentationlayer.domain.voToBo
import es.plexus.android.plexuschuck.presentationlayer.feature.login.LoginContract.Action
import es.plexus.android.plexuschuck.presentationlayer.feature.login.view.state.LoginState
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * This [BaseMvvmViewModel] handles the 'login' feature view-model. Therefore, it is in charge of
 * the logic which allows a user to log-in and/or register. It uses a [LoginDomainLayerBridge] which
 * gathers all the operations available for this entity.
 *
 * All results update an observable variable, [_screenState], with [LoginState] values.
 */
@ExperimentalCoroutinesApi
class LoginViewModel(bridge: LoginDomainLayerBridge<UserLoginBo, Boolean>) :
    BaseMvvmViewModel<LoginDomainLayerBridge<UserLoginBo, Boolean>, LoginState>(bridge = bridge) {

    /**
     * Represents a user interaction, particularly a button click or tap. According to the [Action]
     * input argument, an operation is invoked either for 'login' or 'register'.
     *
     * @param [action] - the purpose of this event
     * @param [userData] - the user data to be used
     */
    fun onButtonSelected(action: Action, userData: UserLoginVo) {
        _screenState.value = ScreenState.Loading
        when (action) {
            Action.LOGIN -> loginUserWithData(userData = userData)
            Action.REGISTER -> registerUserWithData(userData = userData)
        }
    }

    /**
     * Represents a user interaction, particularly a tab toggle displaying a new init mode, either
     * 'login' or 'register'.
     *
     * @param [isLoginMode] - a flag indicating whether the actual mode is 'login' or not
     */
    fun onToggleModeTapped(isLoginMode: Boolean) {
        _screenState.value =
            ScreenState.Render(if (isLoginMode) LoginState.Register else LoginState.Login)
    }

    private fun loginUserWithData(userData: UserLoginVo) {
        bridge.loginUser(
            scope = viewModelScope, params = userData.voToBo(),
            onResult = {
                it.fold(::handleError, ::handleSuccess)
            }
        )
    }

    private fun registerUserWithData(userData: UserLoginVo) {
        bridge.registerUser(
            scope = viewModelScope, params = userData.voToBo(),
            onResult = {
                it.fold(::handleError, ::handleSuccess)
            }
        )
    }

    private fun handleSuccess(isSuccessful: Boolean) {
        _screenState.value = if (isSuccessful) {
            ScreenState.Render(LoginState.AccessGranted)
        } else {
            ScreenState.Render(LoginState.ShowError(failure = FailureVo.Unknown))
        }
    }

    private fun handleError(failureBo: FailureBo) {
        _screenState.value = ScreenState.Render(LoginState.ShowError(failureBo.boToVoFailure()))
    }

}

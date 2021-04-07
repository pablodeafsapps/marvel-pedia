package es.plexus.android.plexuschuck.presentationlayer.feature.login.view.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.plexus.android.plexuschuck.domainlayer.domain.UserLoginBo
import es.plexus.android.plexuschuck.domainlayer.feature.login.LoginDomainLayerBridge
import es.plexus.android.plexuschuck.presentationlayer.R
import es.plexus.android.plexuschuck.presentationlayer.base.BaseMvvmView
import es.plexus.android.plexuschuck.presentationlayer.base.ScreenState
import es.plexus.android.plexuschuck.presentationlayer.databinding.ActivityLoginBinding
import es.plexus.android.plexuschuck.presentationlayer.domain.FailureVo
import es.plexus.android.plexuschuck.presentationlayer.domain.UserLoginVo
import es.plexus.android.plexuschuck.presentationlayer.feature.login.LoginContract.Action
import es.plexus.android.plexuschuck.presentationlayer.feature.login.view.state.LoginState
import es.plexus.android.plexuschuck.presentationlayer.feature.login.viewmodel.LoginViewModel
import es.plexus.android.plexuschuck.presentationlayer.feature.main.view.ui.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.android.viewmodel.ext.android.viewModel

private const val EMPTY_STRING = ""

/**
 * This [AppCompatActivity] represents the login feature of the application. It is here where the
 * user can procceed to log-in or register his account.
 *
 * The UI state is controlled thanks to the collection of a [viewModel] observable variable.
 */
@ExperimentalCoroutinesApi
class LoginActivity :
    AppCompatActivity(),
    BaseMvvmView<LoginViewModel, LoginDomainLayerBridge<UserLoginBo, Boolean>, LoginState> {

    override val viewModel: LoginViewModel by viewModel()
    private lateinit var viewBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityLoginBinding.inflate(layoutInflater)
        initModel()
        initView()
        setContentView(viewBinding.root)
    }

    override fun processRenderState(renderState: LoginState) {
        when (renderState) {
            is LoginState.Login -> showLoginUi()
            is LoginState.Register -> showRegisterUi()
            is LoginState.AccessGranted -> navigateToMainActivity()
            is LoginState.ShowError -> showError(failure = renderState.failure)
        }
    }

    override fun initModel() {
        lifecycleScope.launch {
            viewModel.screenState.collect { screenState ->
                when (screenState) {
                    is ScreenState.Idle -> hideLoading()
                    is ScreenState.Loading -> showLoading()
                    is ScreenState.Render<LoginState> -> {
                        processRenderState(screenState.renderState)
                        hideLoading()
                    }
                }
            }
        }
    }

    private fun initView() {
        with(viewBinding) {
            btnLogin.setOnClickListener {
                // correct login: pablo@mytest.com, pablomytest
                viewModel.onButtonSelected(
                    action = Action.LOGIN,
                    userData = UserLoginVo(
                        email = etEmail.text?.toString(), password = etPassword.text?.toString()
                    )
                )
            }
            btnRegister.setOnClickListener {
                viewModel.onButtonSelected(
                    action = Action.REGISTER,
                    userData = UserLoginVo(
                        email = etEmail.text?.toString(), password = etPassword.text?.toString()
                    )
                )
            }
            tbAccessMode.setOnClickListener {
                viewModel.onToggleModeTapped(btnLogin.visibility == View.VISIBLE)
            }
        }
    }

    private fun showLoginUi() {
        with(viewBinding) {
            tvTitle.text = getString(R.string.tv_login_text)
            btnLogin.visibility = View.VISIBLE
            btnRegister.visibility = View.INVISIBLE
        }
    }

    private fun showRegisterUi() {
        with(viewBinding) {
            tvTitle.text = getString(R.string.tv_register_text)
            btnLogin.visibility = View.INVISIBLE
            btnRegister.visibility = View.VISIBLE
        }
    }

    private fun showLoading() {
        with(viewBinding) {
            pbLoading.visibility = View.VISIBLE
            etEmail.isEnabled = false
            etPassword.isEnabled = false
            btnLogin.isEnabled = false
            tbAccessMode.isEnabled = false
            btnRegister.isEnabled = false
        }
    }

    private fun hideLoading() {
        with(viewBinding) {
            pbLoading.visibility = View.GONE
            etEmail.isEnabled = true
            etPassword.isEnabled = true
            etPassword.setText(EMPTY_STRING)
            btnLogin.isEnabled = true
            tbAccessMode.isEnabled = true
            btnRegister.isEnabled = true
        }
    }

    private fun navigateToMainActivity() {
        startActivity<MainActivity>()
    }

    private fun showError(failure: FailureVo?) {
        if (failure?.getErrorMessage() != null) {
            toast(failure.getErrorMessage())
        }
    }

}

package es.plexus.android.plexuschuck.presentationlayer.feature.splash.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.plexus.android.plexuschuck.domainlayer.base.BaseDomainLayerBridge
import es.plexus.android.plexuschuck.presentationlayer.base.BaseMvvmView
import es.plexus.android.plexuschuck.presentationlayer.base.ScreenState
import es.plexus.android.plexuschuck.presentationlayer.feature.login.view.ui.LoginActivity
import es.plexus.android.plexuschuck.presentationlayer.feature.splash.view.state.SplashState
import es.plexus.android.plexuschuck.presentationlayer.feature.splash.viewmodel.SplashActivityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.jetbrains.anko.startActivity
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * This [AppCompatActivity] represents the typical splash screen used to load the application
 */
@ExperimentalCoroutinesApi
class SplashActivity :
    AppCompatActivity(),
    BaseMvvmView<SplashActivityViewModel, BaseDomainLayerBridge.None, SplashState> {

    override val viewModel: SplashActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onViewCreated()
    }

    override fun processRenderState(renderState: SplashState) {
        when (renderState) {
            is SplashState.LoadingFinished -> startLoginActivity()
        }
    }

    override fun initModel() {
        lifecycleScope.launch {
            viewModel.screenState.collect { screenState ->
                when (screenState) {
                    is ScreenState.Render<SplashState> -> processRenderState(screenState.renderState)
                    else -> {}
                }
            }
        }
    }

    private fun startLoginActivity() {
        startActivity<LoginActivity>()
        finish()
    }

}

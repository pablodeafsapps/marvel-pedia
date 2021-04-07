package es.plexus.android.plexuschuck.presentationlayer.di

import es.plexus.android.plexuschuck.domainlayer.base.BaseDomainLayerBridge
import es.plexus.android.plexuschuck.domainlayer.feature.login.LOGIN_DOMAIN_BRIDGE_TAG
import es.plexus.android.plexuschuck.domainlayer.feature.main.MAIN_DOMAIN_BRIDGE_TAG
import es.plexus.android.plexuschuck.presentationlayer.feature.detail.viewmodel.DetailViewModel
import es.plexus.android.plexuschuck.presentationlayer.feature.login.viewmodel.LoginViewModel
import es.plexus.android.plexuschuck.presentationlayer.feature.main.viewmodel.MainViewModel
import es.plexus.android.plexuschuck.presentationlayer.feature.splash.viewmodel.SplashActivityViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * This variable represents the 'presentation-layer' dependencies module to be used by Koin. It
 * basically includes ViewModel definitions.
 *
 * @author Pablo L. Sordo
 * @since 1.0
 */
@ExperimentalCoroutinesApi
val presentationLayerModule = module(override = true) {
    viewModel { SplashActivityViewModel(bridge = BaseDomainLayerBridge.None) }
    viewModel { LoginViewModel(bridge = get(named(name = LOGIN_DOMAIN_BRIDGE_TAG))) }
    viewModel { MainViewModel(bridge = get(named(name = MAIN_DOMAIN_BRIDGE_TAG))) }
    viewModel { DetailViewModel(bridge = BaseDomainLayerBridge.None) }
}

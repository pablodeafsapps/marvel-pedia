package es.plexus.android.plexuschuck.domainlayer.di

import es.plexus.android.plexuschuck.domainlayer.DomainlayerContract
import es.plexus.android.plexuschuck.domainlayer.DomainlayerContract.Datalayer.Companion.AUTHENTICATION_REPOSITORY_TAG
import es.plexus.android.plexuschuck.domainlayer.DomainlayerContract.Datalayer.Companion.DATA_REPOSITORY_TAG
import es.plexus.android.plexuschuck.domainlayer.domain.JokeBoWrapper
import es.plexus.android.plexuschuck.domainlayer.domain.UserLoginBo
import es.plexus.android.plexuschuck.domainlayer.feature.login.LOGIN_DOMAIN_BRIDGE_TAG
import es.plexus.android.plexuschuck.domainlayer.feature.login.LoginDomainLayerBridge
import es.plexus.android.plexuschuck.domainlayer.feature.login.LoginDomainLayerBridgeImpl
import es.plexus.android.plexuschuck.domainlayer.feature.main.MAIN_DOMAIN_BRIDGE_TAG
import es.plexus.android.plexuschuck.domainlayer.feature.main.MainDomainLayerBridge
import es.plexus.android.plexuschuck.domainlayer.feature.main.MainDomainLayerBridgeImpl
import es.plexus.android.plexuschuck.domainlayer.usecase.FETCH_JOKES_UC_TAG
import es.plexus.android.plexuschuck.domainlayer.usecase.FetchJokesUc
import es.plexus.android.plexuschuck.domainlayer.usecase.LOGIN_UC_TAG
import es.plexus.android.plexuschuck.domainlayer.usecase.LoginUserUc
import es.plexus.android.plexuschuck.domainlayer.usecase.REGISTER_UC_TAG
import es.plexus.android.plexuschuck.domainlayer.usecase.RegisterUserUc
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * This variable represents the 'domain-layer' dependencies module to be used by Koin. It basically
 * includes bridge and use-case definitions.
 *
 * @author Pablo L. Sordo
 * @since 1.0
 */
val domainLayerModule = module(override = true) {
    // bridge
    factory<LoginDomainLayerBridge<UserLoginBo, Boolean>>(named(name = LOGIN_DOMAIN_BRIDGE_TAG)) {
        LoginDomainLayerBridgeImpl(
            loginUserUc = get(named(name = LOGIN_UC_TAG)),
            registerUserUc = get(named(name = REGISTER_UC_TAG))
        )
    }
    factory<MainDomainLayerBridge<JokeBoWrapper>>(named(name = MAIN_DOMAIN_BRIDGE_TAG)) {
        MainDomainLayerBridgeImpl(fetchJokesUc = get(named(name = FETCH_JOKES_UC_TAG)))
    }
    // use-case
    factory<DomainlayerContract.Presentationlayer.UseCase<UserLoginBo, Boolean>>(named(name = LOGIN_UC_TAG)) {
        LoginUserUc(authenticationRepository = get(named(name = AUTHENTICATION_REPOSITORY_TAG)))
    }
    factory<DomainlayerContract.Presentationlayer.UseCase<UserLoginBo, Boolean>>(named(name = REGISTER_UC_TAG)) {
        RegisterUserUc(authenticationRepository = get(named(name = AUTHENTICATION_REPOSITORY_TAG)))
    }
    factory<DomainlayerContract.Presentationlayer.UseCase<Nothing, JokeBoWrapper>>(named(name = FETCH_JOKES_UC_TAG)) {
        FetchJokesUc(dataRepository = get(named(name = DATA_REPOSITORY_TAG)))
    }
}

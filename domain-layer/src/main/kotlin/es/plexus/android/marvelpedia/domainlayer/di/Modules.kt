package es.plexus.android.marvelpedia.domainlayer.di

import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract.Datalayer.Companion.DATA_REPOSITORY_TAG
import es.plexus.android.marvelpedia.domainlayer.feature.main.MAIN_DOMAIN_BRIDGE_TAG
import es.plexus.android.marvelpedia.domainlayer.feature.main.MainDomainLayerBridge
import es.plexus.android.marvelpedia.domainlayer.feature.main.MainDomainLayerBridgeImpl
import es.plexus.android.marvelpedia.domainlayer.usecase.FETCH_CHARACTERS_UC_TAG
import es.plexus.android.marvelpedia.domainlayer.usecase.FetchCharactersUc
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * This variable represents the 'domain-layer' dependencies module to be used by Koin. It basically
 * includes bridge and use-case definitions.
 *
 * @since 1.0
 */
val domainLayerModule = module(override = true) {
    // bridge
    factory<MainDomainLayerBridge<String>>(named(name = MAIN_DOMAIN_BRIDGE_TAG)) {
        MainDomainLayerBridgeImpl(fetchJokesUc = get(named(name = FETCH_CHARACTERS_UC_TAG)))
    }
    // use-case
    factory<DomainlayerContract.Presentationlayer.UseCase<Nothing, String>>(named(name = FETCH_CHARACTERS_UC_TAG)) {
        FetchCharactersUc(dataRepository = get(named(name = DATA_REPOSITORY_TAG)))
    }
}

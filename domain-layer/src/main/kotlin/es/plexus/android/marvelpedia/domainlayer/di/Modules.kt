package es.plexus.android.marvelpedia.domainlayer.di

import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract.Datalayer.Companion.DATA_REPOSITORY_TAG
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterBo
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.feature.detail.DETAIL_DOMAIN_BRIDGE_TAG
import es.plexus.android.marvelpedia.domainlayer.feature.detail.DetailDomainLayerBridge
import es.plexus.android.marvelpedia.domainlayer.feature.detail.DetailDomainLayerBridgeImpl
import es.plexus.android.marvelpedia.domainlayer.feature.main.MAIN_DOMAIN_BRIDGE_TAG
import es.plexus.android.marvelpedia.domainlayer.feature.main.MainDomainLayerBridge
import es.plexus.android.marvelpedia.domainlayer.feature.main.MainDomainLayerBridgeImpl
import es.plexus.android.marvelpedia.domainlayer.usecase.FETCH_CHARACTERS_UC_TAG
import es.plexus.android.marvelpedia.domainlayer.usecase.FETCH_CHARACTER_DETAILS_BY_ID_UC_TAG
import es.plexus.android.marvelpedia.domainlayer.usecase.FetchCharacterDetailsByIdUc
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
    factory<MainDomainLayerBridge<CharacterDataBoWrapper>>(named(name = MAIN_DOMAIN_BRIDGE_TAG)) {
        MainDomainLayerBridgeImpl(fetchCharactersUc = get(named(name = FETCH_CHARACTERS_UC_TAG)))
    }
    factory<DetailDomainLayerBridge<CharacterBo>>(named(name = DETAIL_DOMAIN_BRIDGE_TAG)) {
        DetailDomainLayerBridgeImpl(
            fetchCharacterDetailsByIdUc = get(named(name = FETCH_CHARACTER_DETAILS_BY_ID_UC_TAG))
        )
    }
    // use-case
    factory<DomainlayerContract.Presentationlayer.UseCase<Nothing, CharacterDataBoWrapper>>(
        named(name = FETCH_CHARACTERS_UC_TAG)
    ) {
        FetchCharactersUc(dataRepository = get(named(name = DATA_REPOSITORY_TAG)))
    }
    factory<DomainlayerContract.Presentationlayer.UseCase<CharacterBo, CharacterBo>>(
        named(name = FETCH_CHARACTER_DETAILS_BY_ID_UC_TAG)
    ) {
        FetchCharacterDetailsByIdUc(dataRepository = get(named(name = DATA_REPOSITORY_TAG)))
    }
}

package es.plexus.android.marvelpedia.presentationlayer.feature.main.viewmodel

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo
import es.plexus.android.marvelpedia.domainlayer.feature.main.MAIN_DOMAIN_BRIDGE_TAG
import es.plexus.android.marvelpedia.domainlayer.feature.main.MainDomainLayerBridge
import es.plexus.android.marvelpedia.presentationlayer.base.ScreenState
import es.plexus.android.marvelpedia.presentationlayer.di.presentationLayerModule
import es.plexus.android.marvelpedia.presentationlayer.feature.main.view.state.MainState
import es.plexus.android.marvelpedia.presentationlayer.utils.getDummyCharacterBoWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

@ExperimentalCoroutinesApi
class MainViewModelTest : KoinTest {

    private val viewModel: MainViewModel by inject()
    private lateinit var mockBridge: MainDomainLayerBridge<CharacterDataBoWrapper>

    @Before
    fun setUp() {
        mockBridge = mock()
        startKoin {
            modules(
                listOf(
                    presentationLayerModule,
                    module(override = true) {
                        factory(named(name = MAIN_DOMAIN_BRIDGE_TAG)) { mockBridge }
                    }
                ))
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `check that state is 'ShowCharacterList' when character data are fetched`() {
        // given
        val captor = argumentCaptor<(Either<FailureBo, CharacterDataBoWrapper>) -> Unit>()
        // when
        viewModel.onViewCreated()
        // then
        verify(mockBridge).fetchCharacters(any(), captor.capture())
        verifyNoMoreInteractions(mockBridge)
        captor.firstValue.invoke(getDummyCharacterBoWrapper().right())

        Assert.assertTrue(getRenderState() is MainState.ShowCharacterList)
    }

    @Test
    fun `check that state is 'ShowError' when character data cannot be fetched`() {
        // given
        val captor = argumentCaptor<(Either<FailureBo, CharacterDataBoWrapper>) -> Unit>()
        // when
        viewModel.onViewCreated()
        // then
        verify(mockBridge).fetchCharacters(any(), captor.capture())
        verifyNoMoreInteractions(mockBridge)
        captor.firstValue.invoke(FailureBo.Unknown.left())

        Assert.assertTrue(getRenderState() is MainState.ShowError)
    }

    private fun getRenderState() =
        (viewModel.screenState.value as? ScreenState.Render<MainState>)?.renderState

}

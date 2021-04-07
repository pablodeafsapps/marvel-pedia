package es.plexus.android.plexuschuck.presentationlayer.feature.main.viewmodel

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import es.plexus.android.plexuschuck.domainlayer.domain.FailureBo
import es.plexus.android.plexuschuck.domainlayer.domain.JokeBo
import es.plexus.android.plexuschuck.domainlayer.domain.JokeBoWrapper
import es.plexus.android.plexuschuck.domainlayer.feature.main.MAIN_DOMAIN_BRIDGE_TAG
import es.plexus.android.plexuschuck.domainlayer.feature.main.MainDomainLayerBridge
import es.plexus.android.plexuschuck.presentationlayer.base.ScreenState
import es.plexus.android.plexuschuck.presentationlayer.di.presentationLayerModule
import es.plexus.android.plexuschuck.presentationlayer.feature.main.view.state.MainState
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

private const val DEFAULT_STRING_VALUE = ""
private const val DEFAULT_INTEGER_VALUE = -1

@ExperimentalCoroutinesApi
class MainViewModelTest : KoinTest {

    private val viewModel: MainViewModel by inject()
    private lateinit var mockBridge: MainDomainLayerBridge<JokeBoWrapper>

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
    fun `check that state is 'ShowJokeList' when jokes are fetched`() {
        // given
        val captor = argumentCaptor<(Either<FailureBo, JokeBoWrapper>) -> Unit>()
        // when
        viewModel.onViewCreated()
        // then
        verify(mockBridge).fetchJokes(any(), captor.capture())
        verifyNoMoreInteractions(mockBridge)
        captor.firstValue.invoke(getDummyJokeBoWrapper().right())

        Assert.assertTrue(getRenderState() is MainState.ShowJokeList)
    }

    @Test
    fun `check that state is 'ShowError' when jokes cannot be fetched`() {
        // given
        val captor = argumentCaptor<(Either<FailureBo, JokeBoWrapper>) -> Unit>()
        // when
        viewModel.onViewCreated()
        // then
        verify(mockBridge).fetchJokes(any(), captor.capture())
        verifyNoMoreInteractions(mockBridge)
        captor.firstValue.invoke(FailureBo.Unknown.left())

        Assert.assertTrue(getRenderState() is MainState.ShowError)
    }

    private fun getRenderState() =
        (viewModel.screenState.value as? ScreenState.Render<MainState>)?.renderState

    private fun getDummyJokeBoWrapper() = JokeBoWrapper(
        type = DEFAULT_STRING_VALUE,
        value = getDummyJokeBoList()
    )

    private fun getDummyJokeBoList() = listOf(getDummyJokeBo())

    private fun getDummyJokeBo() = JokeBo(
        id = DEFAULT_INTEGER_VALUE,
        joke = DEFAULT_STRING_VALUE,
        categories = listOf(DEFAULT_STRING_VALUE)
    )

}

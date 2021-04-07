package es.plexus.android.marvelpedia.presentationlayer

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.nhaarman.mockitokotlin2.mock
import es.plexus.android.marvelpedia.domainlayer.di.domainLayerModule
import es.plexus.android.marvelpedia.domainlayer.domain.JokeBoWrapper
import es.plexus.android.marvelpedia.domainlayer.feature.main.MAIN_DOMAIN_BRIDGE_TAG
import es.plexus.android.marvelpedia.domainlayer.feature.main.MainDomainLayerBridge
import es.plexus.android.marvelpedia.presentationlayer.di.presentationLayerModule
import es.plexus.android.marvelpedia.presentationlayer.feature.main.view.ui.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest

@ExperimentalCoroutinesApi
class MainActivityInstrumentationTest : KoinTest {

    private lateinit var scenario: ActivityScenario<MainActivity>
    private val mockBridge = mock<MainDomainLayerBridge<JokeBoWrapper>>()

    @Before
    fun setUp() {
        startKoin {
            modules(listOf(
                presentationLayerModule, domainLayerModule,
                module(override = true) {
                    factory(named(name = MAIN_DOMAIN_BRIDGE_TAG)) { mockBridge }
                }
            ))
        }
        scenario = ActivityScenario.launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        stopKoin()
        scenario.close()
    }

    @Test
    fun whenActivityStartsProgressBarIsDisplayed() {
        onView(withId(R.id.pbLoading)).check(matches(isDisplayed()))
    }

}

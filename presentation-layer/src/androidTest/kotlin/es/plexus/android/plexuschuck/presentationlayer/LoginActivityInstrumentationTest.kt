package es.plexus.android.plexuschuck.presentationlayer

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.nhaarman.mockitokotlin2.mock
import es.plexus.android.plexuschuck.domainlayer.di.domainLayerModule
import es.plexus.android.plexuschuck.domainlayer.domain.UserLoginBo
import es.plexus.android.plexuschuck.domainlayer.feature.login.LOGIN_DOMAIN_BRIDGE_TAG
import es.plexus.android.plexuschuck.domainlayer.feature.login.LoginDomainLayerBridge
import es.plexus.android.plexuschuck.presentationlayer.di.presentationLayerModule
import es.plexus.android.plexuschuck.presentationlayer.feature.login.view.ui.LoginActivity
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
class LoginActivityInstrumentationTest : KoinTest {

    private lateinit var scenario: ActivityScenario<LoginActivity>
    private val mockBridge = mock<LoginDomainLayerBridge<UserLoginBo, Boolean>>()

    @Before
    fun setUp() {
        startKoin {
            modules(listOf(
                presentationLayerModule, domainLayerModule,
                module(override = true) {
                    factory(named(name = LOGIN_DOMAIN_BRIDGE_TAG)) { mockBridge }
                }
            ))
        }
        scenario = ActivityScenario.launch(LoginActivity::class.java)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun whenActivityStartsLoginIsDisplayed() {
        // when // then
        onView(withId(R.id.tvTitle)).check(matches(isDisplayed()))
    }

    @Test
    fun whenActivityStartsAndRegisterIsTappedRegisterTitleIsDisplayed() {
        // given
        val requiredText = "Register"
        // when
        onView(withId(R.id.tbAccessMode)).perform(click())
        // then
        onView(withId(R.id.tvTitle)).check(matches(withText(requiredText)))
    }

    @Test
    fun whenEditTextsAreFilledUpLoginButtonIsDisplayed() {
        // given
        val requiredEmail = "pablo@mytest.es"
        val requiredPassword = "pablomytest"
        // when
        onView(withId(R.id.etEmail)).perform(clearText(), typeText(requiredEmail))
        onView(withId(R.id.etPassword)).perform(clearText(), typeText(requiredPassword))
        // then
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()))
    }

}

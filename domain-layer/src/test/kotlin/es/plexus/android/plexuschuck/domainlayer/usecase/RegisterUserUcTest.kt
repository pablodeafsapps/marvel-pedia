package es.plexus.android.plexuschuck.domainlayer.usecase

import arrow.core.Either
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import es.plexus.android.plexuschuck.domainlayer.DomainlayerContract
import es.plexus.android.plexuschuck.domainlayer.di.domainLayerModule
import es.plexus.android.plexuschuck.domainlayer.domain.FailureBo
import es.plexus.android.plexuschuck.domainlayer.domain.UserLoginBo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

@ExperimentalCoroutinesApi
class RegisterUserUcTest : KoinTest {

    private val registerUserUc: DomainlayerContract.Presentationlayer.UseCase<UserLoginBo, Boolean>
            by inject(named(name = REGISTER_UC_TAG))
    private lateinit var mockRepository: DomainlayerContract.Datalayer.AuthenticationRepository<UserLoginBo, Boolean>

    @Before
    fun setUp() {
        mockRepository = mock()
        // adding that dependency to the DI graph, since it is in a different module (overriding)
        startKoin {
            modules(listOf(
                domainLayerModule,
                module {
                    single(named(name = DomainlayerContract.Datalayer.AUTHENTICATION_REPOSITORY_TAG)) { mockRepository }
                }
            ))
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `check that if user login data are not null or empty, registerUer is invoked`() =
        runBlockingTest {
            // given
            val rightParams = UserLoginBo(email = "example@plexus.es", password = "password")
            // when
            registerUserUc.run(params = rightParams)
            // then
            verify(mockRepository).registerUser(params = eq(rightParams))
        }

    @Test
    fun `check that if user login data are null, error is returned`() = runBlockingTest {
        // given
        val nullParams = null
        // when
        val response = registerUserUc.run(params = nullParams)
        // then
        assertTrue((response as? Either.Left<FailureBo>)?.a is FailureBo.InputParamsError)
    }

    @Test
    fun `check that if any user login data is empty, error is returned`() = runBlockingTest {
        // given
        val wrongParms = UserLoginBo(email = "", password = "password")
        // when
        val response = registerUserUc.run(params = wrongParms)
        // then
        assertTrue((response as? Either.Left<FailureBo>)?.a is FailureBo.InputParamsError)
    }

}

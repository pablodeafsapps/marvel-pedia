package es.plexus.android.marvelpedia.domainlayer.usecase

import arrow.core.Either
import arrow.core.Right
import arrow.core.right
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract.Datalayer.Companion.AUTHENTICATION_REPOSITORY_TAG
import es.plexus.android.marvelpedia.domainlayer.di.domainLayerModule
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo
import es.plexus.android.marvelpedia.domainlayer.domain.UserLoginBo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
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
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class LoginUserUcTest : KoinTest {

    private val loginUserUc: DomainlayerContract.Presentationlayer.UseCase<UserLoginBo, Boolean>
            by inject(named(name = LOGIN_UC_TAG))
    private lateinit var mockRepository: DomainlayerContract.Datalayer.AuthenticationRepository<UserLoginBo, Boolean>

    @Before
    fun setUp() {
        mockRepository = mock()
        // adding that dependency to the DI graph, since it is in a different module (overriding)
        startKoin {
            modules(listOf(
                domainLayerModule,
                module { single(named(name = AUTHENTICATION_REPOSITORY_TAG)) { mockRepository } }
            ))
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `check that if user login data are not null or empty, loginUser is invoked`() = runBlockingTest {
        // given
        val rightParams = UserLoginBo(email = "example@plexus.es", password = "password")
        // when
        loginUserUc.run(params = rightParams)
        // then
        verify(mockRepository).loginUser(params = eq(rightParams))
    }

    @Test
    fun `check that if user login data are null, error is returned`() = runBlockingTest {
        // given
        val nullParams = null
        // when
        val response = loginUserUc.run(params = nullParams)
        // then
        Assert.assertTrue((response as? Either.Left<FailureBo>)?.a is FailureBo.InputParamsError)
    }

    @Test
    fun `check that if any user login data is empty, error is returned`() = runBlockingTest {
        // given
        val wrongParms = UserLoginBo(email = "", password = "password")
        // when
        val response = loginUserUc.run(params = wrongParms)
        // then
        Assert.assertTrue((response as? Either.Left<FailureBo>)?.a is FailureBo.InputParamsError)
    }

    @Test
    fun `check that if user is correct, true is returned`() = runBlockingTest {
        // given
        val rightParams = UserLoginBo(email = "example@plexus.es", password = "password")
        val callbackMock = mock<(Either<FailureBo, Boolean>) -> Unit>()
        val dispatcherWorker = TestCoroutineDispatcher()
        // when
        Mockito.`when`(mockRepository.loginUser(rightParams)).thenReturn(true.right())
        loginUserUc.invoke(
            params = rightParams,
            scope = this,
            onResult = callbackMock,
            dispatcherWorker = dispatcherWorker
        )
        // then
        verify(callbackMock).invoke(Right(true))

    }

}

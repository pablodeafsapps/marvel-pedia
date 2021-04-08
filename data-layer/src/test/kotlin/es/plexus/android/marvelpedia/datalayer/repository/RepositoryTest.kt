package es.plexus.android.marvelpedia.datalayer.repository

import com.nhaarman.mockitokotlin2.mock
import es.plexus.android.marvelpedia.datalayer.datasource.ConnectivityDataSource
import es.plexus.android.marvelpedia.datalayer.datasource.ConnectivityDataSource.Companion.CONNECTIVITY_DATA_SOURCE_TAG
import es.plexus.android.marvelpedia.datalayer.di.dataLayerModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest

@ExperimentalCoroutinesApi
class RepositoryTest : KoinTest {

    private lateinit var mockConnectivityDataSource: ConnectivityDataSource

    @Before
    fun setUp() {
        // create data-source mock
        mockConnectivityDataSource = mock()
        startKoin {
            modules(listOf(
                dataLayerModule,
                module(override = true) {
                    factory(named(name = CONNECTIVITY_DATA_SOURCE_TAG)) { mockConnectivityDataSource }
                }
            ))
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    /*
    @Test
    fun `check that if connection is KO, a 'NoConnection' error is returned`() =
        runBlockingTest {
            // given
            val rightParams = getDummyUserLoginBo()
            whenever(mockConnectivityDataSource.checkNetworkConnectionAvailability()).doReturn(false)
            // when
            val actualResult = authenticationRepository.loginUser(params = rightParams)
            // then
            Assert.assertTrue((actualResult as? Either.Left<FailureBo>)?.a is FailureBo.NoConnection)
        }

    @Test
    fun `check that if connection is OK, and data-source response fails, a login error is returned`() =
        runBlockingTest {
            // given
            val rightParams = getDummyUserLoginBo()
            whenever(mockConnectivityDataSource.checkNetworkConnectionAvailability()).doReturn(true)
            whenever(mockAuthenticationDataSource.requestLogin(userData = any())).doReturn(
                FailureDto.FirebaseLoginError.left()
            )
            // when
            val actualResult = authenticationRepository.loginUser(params = rightParams)
            // then
            Assert.assertTrue((actualResult as? Either.Left<FailureBo>)?.a is FailureBo.ServerError)
        }

    @Test
    fun `check that if connection is OK, and data-source response throws an exception, an 'Unknown' error is returned`() =
        runBlockingTest {
            // given
            val rightParams = getDummyUserLoginBo()
            whenever(mockConnectivityDataSource.checkNetworkConnectionAvailability()).doReturn(true)
            whenever(mockAuthenticationDataSource.requestLogin(userData = any())).doThrow(
                IllegalStateException()
            )
            // when
            val actualResult = authenticationRepository.loginUser(params = rightParams)
            // then
            Assert.assertTrue((actualResult as? Either.Left<FailureBo>)?.a is FailureBo.Unknown)
        }

    @Test
    fun `check that if connection is OK, and data-source response is successful, a Boolean is returned`() =
        runBlockingTest {
            // given
            val rightParams = getDummyUserLoginBo()
            whenever(mockConnectivityDataSource.checkNetworkConnectionAvailability()).doReturn(true)
            whenever(mockAuthenticationDataSource.requestLogin(userData = any())).doReturn(true.right())
            // when
            val actualResult = authenticationRepository.loginUser(params = rightParams)
            // then
            Assert.assertTrue((actualResult as? Either.Right<Boolean>) != null)
        }

    private fun getDummyUserLoginBo() = UserLoginBo(
        email = "email@example.com",
        password = "password"
    )
    */

}

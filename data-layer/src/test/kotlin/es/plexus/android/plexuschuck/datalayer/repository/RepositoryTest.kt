package es.plexus.android.plexuschuck.datalayer.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import es.plexus.android.plexuschuck.datalayer.datasource.AuthenticationDataSource
import es.plexus.android.plexuschuck.datalayer.datasource.AuthenticationDataSource.Companion.AUTHENTICATION_DATA_SOURCE_TAG
import es.plexus.android.plexuschuck.datalayer.datasource.ConnectivityDataSource
import es.plexus.android.plexuschuck.datalayer.datasource.ConnectivityDataSource.Companion.CONNECTIVITY_DATA_SOURCE_TAG
import es.plexus.android.plexuschuck.datalayer.di.dataLayerModule
import es.plexus.android.plexuschuck.datalayer.domain.FailureDto
import es.plexus.android.plexuschuck.domainlayer.DomainlayerContract
import es.plexus.android.plexuschuck.domainlayer.DomainlayerContract.Datalayer.Companion.AUTHENTICATION_REPOSITORY_TAG
import es.plexus.android.plexuschuck.domainlayer.domain.FailureBo
import es.plexus.android.plexuschuck.domainlayer.domain.UserLoginBo
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@ExperimentalCoroutinesApi
class RepositoryTest : KoinTest {

    private val authenticationRepository: DomainlayerContract.Datalayer.AuthenticationRepository<UserLoginBo, Boolean>
            by inject(named(name = AUTHENTICATION_REPOSITORY_TAG))
    private lateinit var mockConnectivityDataSource: ConnectivityDataSource
    private lateinit var mockAuthenticationDataSource: AuthenticationDataSource

    @Before
    fun setUp() {
        // create data-source mock
        mockConnectivityDataSource = mock()
        mockAuthenticationDataSource = mock()
        startKoin {
            modules(listOf(
                dataLayerModule,
                module(override = true) {
                    factory(named(name = CONNECTIVITY_DATA_SOURCE_TAG)) { mockConnectivityDataSource }
                    factory(named(name = AUTHENTICATION_DATA_SOURCE_TAG)) { mockAuthenticationDataSource }
                }
            ))
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

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
                FailureDto.FirebaseLoginError.left())
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
            whenever(mockAuthenticationDataSource.requestLogin(userData = any())).doThrow(IllegalStateException())
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

}

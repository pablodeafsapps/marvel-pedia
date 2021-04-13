package es.plexus.android.marvelpedia.datalayer.repository

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.doThrow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import es.plexus.android.marvelpedia.datalayer.datasource.CharactersDataSource
import es.plexus.android.marvelpedia.datalayer.datasource.CharactersDataSource.Companion.CHARACTERS_DATA_SOURCE_TAG
import es.plexus.android.marvelpedia.datalayer.datasource.ConnectivityDataSource
import es.plexus.android.marvelpedia.datalayer.datasource.ConnectivityDataSource.Companion.CONNECTIVITY_DATA_SOURCE_TAG
import es.plexus.android.marvelpedia.datalayer.di.dataLayerModule
import es.plexus.android.marvelpedia.datalayer.utils.DEFAULT_STRING_VALUE
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract.Datalayer.Companion.DATA_REPOSITORY_TAG
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo
import es.plexus.android.marvelpedia.datalayer.utils.getDummyCharacterBoWrapper
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

    private val dataRepository: DomainlayerContract.Datalayer.DataRepository<CharacterDataBoWrapper>
            by inject(named(name = DATA_REPOSITORY_TAG))
    private lateinit var mockConnectivityDataSource: ConnectivityDataSource
    private lateinit var mockCharactersDataSource: CharactersDataSource

    @Before
    fun setUp() {
        // create data-source mock
        mockConnectivityDataSource = mock()
        mockCharactersDataSource = mock()
        startKoin {
            modules(listOf(
                dataLayerModule,
                module(override = true) {
                    factory(named(name = CONNECTIVITY_DATA_SOURCE_TAG)) { mockConnectivityDataSource }
                    factory(named(name = CHARACTERS_DATA_SOURCE_TAG)) { mockCharactersDataSource }
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
            whenever(mockConnectivityDataSource.checkNetworkConnectionAvailability()).doReturn(false)
            // when
            val actualResult = dataRepository.fetchCharacters()
            // then
            Assert.assertTrue((actualResult as? Either.Left<FailureBo>)?.a is FailureBo.NoConnection)
        }

    @Test
    fun `check that if connection is OK, and data-source response fails, an error is returned`() =
        runBlockingTest {
            // given
            whenever(mockConnectivityDataSource.checkNetworkConnectionAvailability()).doReturn(true)
            whenever(mockCharactersDataSource.fetchCharactersResponse()).doReturn(
                FailureBo.Error(msg = DEFAULT_STRING_VALUE).left()
            )
            // when
            val actualResult = dataRepository.fetchCharacters()
            // then
            Assert.assertTrue((actualResult as? Either.Left<FailureBo>)?.a is FailureBo.Error)
        }

    @Test
    fun `check that if connection is OK, and data-source throws an exception, an 'Unknown' error is returned`() =
        runBlockingTest {
            // given
            whenever(mockConnectivityDataSource.checkNetworkConnectionAvailability()).doReturn(true)
            whenever(mockCharactersDataSource.fetchCharactersResponse()).doThrow(
                IllegalStateException()
            )
            // when
            val actualResult = dataRepository.fetchCharacters()
            // then
            Assert.assertTrue((actualResult as? Either.Left<FailureBo>)?.a is FailureBo.Unknown)
        }

    @Test
    fun `check that if connection is OK, and data-source response is successful, non-null data is returned`() =
        runBlockingTest {
            // given
            whenever(mockConnectivityDataSource.checkNetworkConnectionAvailability()).doReturn(true)
            whenever(mockCharactersDataSource.fetchCharactersResponse()).doReturn(
                getDummyCharacterBoWrapper().right())
            // when
            val actualResult = dataRepository.fetchCharacters()
            // then
            Assert.assertTrue((actualResult as? Either.Right<CharacterDataBoWrapper>) != null)
        }

}

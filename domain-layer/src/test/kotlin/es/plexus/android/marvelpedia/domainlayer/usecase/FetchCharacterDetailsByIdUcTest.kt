package es.plexus.android.marvelpedia.domainlayer.usecase

import arrow.core.Either
import arrow.core.right
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract.Datalayer.Companion.DATA_REPOSITORY_TAG
import es.plexus.android.marvelpedia.domainlayer.di.domainLayerModule
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterBo
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo
import es.plexus.android.marvelpedia.domainlayer.utils.getDummyCharacterBo
import es.plexus.android.marvelpedia.domainlayer.utils.getDummyCharacterBoWrapper
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

class FetchCharacterDetailsByIdUcTest : KoinTest {

    private val fetchCharacterDetailsUc: DomainlayerContract.Presentationlayer.UseCase<CharacterBo, CharacterBo>
            by inject(named(name = FETCH_CHARACTER_DETAILS_BY_ID_UC_TAG))
    private lateinit var mockRepository: DomainlayerContract.Datalayer.DataRepository<CharacterDataBoWrapper>

    @Before
    fun setUp() {
        mockRepository = mock()
        // adding that dependency to the DI graph, since it is in a different module (overriding)
        startKoin {
            modules(listOf(
                domainLayerModule,
                module { single(named(name = DATA_REPOSITORY_TAG)) { mockRepository } }
            ))
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check that if use-case params are not null, repository is queried`() = runBlockingTest {
        // given
        val rightParams = getDummyCharacterBo()
        whenever(mockRepository.fetchCharacterDetailsByIdUc(id = any())).doReturn(getDummyCharacterBoWrapper().right())
        // when
        fetchCharacterDetailsUc.run(params = rightParams)
        // then
        verify(mockRepository).fetchCharacterDetailsByIdUc(id = eq(rightParams.id))
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `check that if user login data are null, error is returned`() = runBlockingTest {
        // given
        val nullParams = null
        // when
        val response = fetchCharacterDetailsUc.run(params = nullParams)
        // then
        Assert.assertTrue((response as? Either.Left<FailureBo>)?.a is FailureBo.InputParamsError)
    }

}

package es.plexus.android.marvelpedia.datalayer.utils

import es.plexus.android.marvelpedia.datalayer.datasource.JokesDataSource.Companion.JOKES_API_SERVICE_TAG
import es.plexus.android.marvelpedia.datalayer.di.dataLayerModule
import es.plexus.android.marvelpedia.datalayer.domain.JokeDtoWrapper
import es.plexus.android.marvelpedia.datalayer.domain.dtoToBo
import es.plexus.android.marvelpedia.datalayer.service.IcndbApiService
import es.plexus.android.marvelpedia.domainlayer.domain.JokeBoWrapper
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.test.KoinTest
import org.koin.test.inject
import retrofit2.Retrofit

class ExtensionUtilsKtTest : KoinTest {

    private val retrofitClient: Retrofit by inject(named(name = JOKES_API_SERVICE_TAG))

    @Before
    fun setUp() {
        startKoin {
            modules(listOf(dataLayerModule))
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `asfasfdsaf`() = runBlockingTest {
        // given
        val request = retrofitClient.create(IcndbApiService::class.java)::getJokesAsync
        val transform: (JokeDtoWrapper) -> JokeBoWrapper = { it.dtoToBo() }
        // when
        val actualResult = retrofitSafeCall(retrofitRequest = request, transform = transform)
        // then
    }

}

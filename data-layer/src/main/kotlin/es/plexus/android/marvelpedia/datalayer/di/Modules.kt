package es.plexus.android.marvelpedia.datalayer.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import es.plexus.android.marvelpedia.datalayer.datasource.AndroidDataSource
import es.plexus.android.marvelpedia.datalayer.datasource.ConnectivityDataSource
import es.plexus.android.marvelpedia.datalayer.datasource.ConnectivityDataSource.Companion.CONNECTIVITY_DATA_SOURCE_TAG
import es.plexus.android.marvelpedia.datalayer.datasource.MarvelDataSource
import es.plexus.android.marvelpedia.datalayer.datasource.MoviesDataSource
import es.plexus.android.marvelpedia.datalayer.datasource.MoviesDataSource.Companion.MOVIES_API_SERVICE_TAG
import es.plexus.android.marvelpedia.datalayer.datasource.MoviesDataSource.Companion.MOVIES_DATA_SOURCE_TAG
import es.plexus.android.marvelpedia.datalayer.datasource.MoviesDataSource.Companion.MOVIES_BASE_URL
import es.plexus.android.marvelpedia.datalayer.repository.Repository
import es.plexus.android.marvelpedia.datalayer.utils.ConnectivityInterceptor
import es.plexus.android.marvelpedia.datalayer.utils.ConnectivityInterceptor.Companion.CONNECTIVITY_INTERCEPTOR_TAG
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract
import es.plexus.android.marvelpedia.domainlayer.DomainlayerContract.Datalayer.Companion.DATA_REPOSITORY_TAG
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECTION_TIMEOUT = 20L
private const val READ_TIMEOUT = 30L

/**
 * This variable represents the 'data-layer' dependencies module to be used by Koin. It basically
 * includes repository and data-source definitions.
 *
 * @since 1.0
 */
val dataLayerModule = module(override = true) {
    // repository
    single {
        Repository.apply {
            connectivityDataSource = get(named(name = CONNECTIVITY_DATA_SOURCE_TAG))
            moviesDataSource = get(named(name = MOVIES_DATA_SOURCE_TAG))
        }
    }
    single<DomainlayerContract.Datalayer.DataRepository<CharacterDataBoWrapper>>(named(name = DATA_REPOSITORY_TAG)) {
        get<Repository>()
    }
    // data-source
    factory<ConnectivityDataSource>(named(name = CONNECTIVITY_DATA_SOURCE_TAG)) {
        AndroidDataSource(context = androidContext())
    }
    factory<MoviesDataSource>(named(name = MOVIES_DATA_SOURCE_TAG)) {
        MarvelDataSource(get(named(name = MOVIES_API_SERVICE_TAG)))
    }
    factory<Interceptor>(named(name = CONNECTIVITY_INTERCEPTOR_TAG)) {
        ConnectivityInterceptor(get(named(name = CONNECTIVITY_DATA_SOURCE_TAG)))
    }
    factory<OkHttpClient> {
        OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(get(named(name = CONNECTIVITY_INTERCEPTOR_TAG)))
            .build()
    }
    // retrofit
    single<Retrofit>(named(name = MOVIES_API_SERVICE_TAG)) {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(get())
            .baseUrl(MOVIES_BASE_URL)
            .build()
    }
}

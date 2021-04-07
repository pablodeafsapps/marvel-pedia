package es.plexus.android.plexuschuck.datalayer.di

import com.google.firebase.auth.FirebaseAuth
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import es.plexus.android.plexuschuck.datalayer.datasource.AndroidDataSource
import es.plexus.android.plexuschuck.datalayer.datasource.AuthenticationDataSource
import es.plexus.android.plexuschuck.datalayer.datasource.AuthenticationDataSource.Companion.AUTHENTICATION_DATA_SOURCE_TAG
import es.plexus.android.plexuschuck.datalayer.datasource.AuthenticationDataSource.Companion.AUTHENTICATOR_TAG
import es.plexus.android.plexuschuck.datalayer.datasource.ConnectivityDataSource
import es.plexus.android.plexuschuck.datalayer.datasource.ConnectivityDataSource.Companion.CONNECTIVITY_DATA_SOURCE_TAG
import es.plexus.android.plexuschuck.datalayer.datasource.FirebaseDataSource
import es.plexus.android.plexuschuck.datalayer.datasource.IcndbDataSource
import es.plexus.android.plexuschuck.datalayer.datasource.JokesDataSource
import es.plexus.android.plexuschuck.datalayer.datasource.JokesDataSource.Companion.ICNDB_BASE_URL
import es.plexus.android.plexuschuck.datalayer.datasource.JokesDataSource.Companion.JOKES_API_SERVICE_TAG
import es.plexus.android.plexuschuck.datalayer.datasource.JokesDataSource.Companion.JOKES_DATA_SOURCE_TAG
import es.plexus.android.plexuschuck.datalayer.repository.Repository
import es.plexus.android.plexuschuck.datalayer.utils.ConnectivityInterceptor
import es.plexus.android.plexuschuck.datalayer.utils.ConnectivityInterceptor.Companion.CONNECTIVITY_INTERCEPTOR_TAG
import es.plexus.android.plexuschuck.domainlayer.DomainlayerContract
import es.plexus.android.plexuschuck.domainlayer.DomainlayerContract.Datalayer.Companion.AUTHENTICATION_REPOSITORY_TAG
import es.plexus.android.plexuschuck.domainlayer.DomainlayerContract.Datalayer.Companion.DATA_REPOSITORY_TAG
import es.plexus.android.plexuschuck.domainlayer.domain.JokeBoWrapper
import es.plexus.android.plexuschuck.domainlayer.domain.UserLoginBo
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
 * @author Pablo L. Sordo
 * @since 1.0
 */
val dataLayerModule = module(override = true) {
    // repository
    single {
        Repository.apply {
            connectivityDataSource = get(named(name = CONNECTIVITY_DATA_SOURCE_TAG))
            authenticationDataSource = get(named(name = AUTHENTICATION_DATA_SOURCE_TAG))
            jokesDataSource = get(named(name = JOKES_DATA_SOURCE_TAG))
        }
    }
    single<DomainlayerContract.Datalayer.AuthenticationRepository<UserLoginBo, Boolean>
            >(named(name = AUTHENTICATION_REPOSITORY_TAG)) {
        get<Repository>()
    }
    single<DomainlayerContract.Datalayer.DataRepository<JokeBoWrapper>>(named(name = DATA_REPOSITORY_TAG)) {
        get<Repository>()
    }
    // data-source
    factory<ConnectivityDataSource>(named(name = CONNECTIVITY_DATA_SOURCE_TAG)) {
        AndroidDataSource(context = androidContext())
    }
    factory<AuthenticationDataSource>(named(name = AUTHENTICATION_DATA_SOURCE_TAG)) {
        FirebaseDataSource(fbAuth = get(named(name = AUTHENTICATOR_TAG)))
    }
    factory<JokesDataSource>(named(name = JOKES_DATA_SOURCE_TAG)) {
        IcndbDataSource(get(named(name = JOKES_API_SERVICE_TAG)))
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
    single<Retrofit>(named(name = JOKES_API_SERVICE_TAG)) {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .client(get())
            .baseUrl(ICNDB_BASE_URL)
            .build()
    }
    // firebase
    single<FirebaseAuth>(named(name = AUTHENTICATOR_TAG)) {
        FirebaseAuth.getInstance()
    }
}

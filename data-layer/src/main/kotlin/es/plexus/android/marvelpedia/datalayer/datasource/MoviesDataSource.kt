package es.plexus.android.marvelpedia.datalayer.datasource

import arrow.core.Either
import es.plexus.android.marvelpedia.datalayer.domain.toBo
import es.plexus.android.marvelpedia.datalayer.service.MarvelApiService
import es.plexus.android.marvelpedia.datalayer.utils.retrofitSafeCall
import es.plexus.android.marvelpedia.domainlayer.domain.CharacterDataBoWrapper
import es.plexus.android.marvelpedia.domainlayer.domain.FailureBo
import retrofit2.Retrofit
import java.math.BigInteger
import java.security.MessageDigest

private const val RADIX = 16
private const val SIGNUM = 1

/**
 * This interface represents the contract to be complied by an entity to fit in as the jokes provider
 */
interface MoviesDataSource {

    companion object {
        const val MOVIES_DATA_SOURCE_TAG = "moviesDataSource"
        const val MOVIES_API_SERVICE_TAG = "moviesApiService"
        const val MOVIES_BASE_URL = "https://gateway.marvel.com/"
    }

    /**
     * Fetches a list of characters in a wrapper or an error otherwise
     */
    suspend fun fetchCharactersResponse(): Either<FailureBo, CharacterDataBoWrapper>

    /**
     * Fetches a specific character according to an identifier or an error otherwise
     */
//    suspend fun fetchCharacterByIdResponse(id: String): Either<FailureBo, String>
}

/**
 *
 */
class MarvelDataSource(private val retrofit: Retrofit) : MoviesDataSource {

    override suspend fun fetchCharactersResponse(): Either<FailureBo, CharacterDataBoWrapper> {
        val timestamp = getCurrentTimestamp()
        return retrofitSafeCall(
            requestParameter = mapOf(
                "ts" to timestamp,
                "apikey" to getMarvelUserApiKey(),
                "hash" to getMd5Hash(RADIX, SIGNUM, timestamp)
            ),
            retrofitRequest = retrofit.create(MarvelApiService::class.java)::getCharactersAsync,
            transform = { it.toBo() }
        )
    }

    private fun getMarvelUserApiKey(): String = "f1316cbd028773f7effb0218b6a29d12"

    private fun getCurrentTimestamp(): String = System.currentTimeMillis().toString()

    private fun getMd5Hash(radix: Int, signum: Int, timestamp: String): String {
        val md = MessageDigest.getInstance("MD5")
        val data =
            timestamp.toByteArray() + "b2a9aec295e4416db64dcbd692490af1ad8a1c5b".toByteArray() +
                    getMarvelUserApiKey().toByteArray()
        return BigInteger(signum, md.digest(data)).toString(radix)
    }

}

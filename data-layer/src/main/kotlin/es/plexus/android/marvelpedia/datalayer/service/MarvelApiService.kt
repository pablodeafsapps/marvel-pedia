package es.plexus.android.marvelpedia.datalayer.service

import es.plexus.android.marvelpedia.datalayer.domain.CharacterDataDtoWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * This interface is used by Retrofit to conform the API service queries
 */
interface MarvelApiService {

    /**
     * This function returns a characters-related response from an HTTP GET query
     */
//    @GET
//    suspend fun getCharactersAsync(@Url url: String): Response<String>

    /**
     * This function returns a characters-related response from an HTTP GET query
     */
    @GET("v1/public/characters")
    suspend fun getCharactersAsync(@QueryMap filters: Map<String, String>): Response<CharacterDataDtoWrapper>

    /**
     * This function returns a single character response from an HTTP GET query, according to a specific identifier
     */
    @GET("v1/public/characters/{characterId}")
    suspend fun getCharacterByIdAsync(
        @Query("apikey") apiKey: String,
        @Query("ts") ts: String,
        @Query("hash") hash: String,
        @Path("characterId") id: String
    ): Response<String>

}

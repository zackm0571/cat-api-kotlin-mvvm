package com.zackmatthews.catapikotlinmvvm

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for api.thecatapi.com. Access through {@link CatRepo}.
 */
interface CatApi {
    /**
     * Gets the complete list of breeds that can be individually used as search filters.
     *
     * @return A list of {@link CatBreed}s.
     */
    @GET("/v1/breeds")
    fun getBreeds(): Call<List<CatBreed>>

    /**
     * Gets the complete list of categories that can be individually used as search filters.
     *
     * @return A list of {@link CatCategory}s.
     */
    @GET("/v1/categories")
    fun getCategories(): Call<List<CatCategory>>

    /**
     * Returns random images.
     *
     * @param n the number of images to return
     * @return
     */
    @GET("/v1/images/search")
    fun getRandomImages(@Query("limit") n: Int): Call<List<CatImage>>

    /**
     * Query the cat api and filter by breed. v1 of thecatapi does not allow querying with multiple breeds, the api will return an empty array.
     *
     * @param n        the number of results to return
     * @param breedIds Comma separated list of id's. As of now the cat api only allows one id.
     * @return A list of {@link CatImage}s.
     */
    @GET("/v1/images/search")
    fun searchByBreed(
        @Query("limit") n: Int,
        @Query("breed_ids", encoded = true) breedIds: String
    ): Call<List<CatImage>>

    /**
     * Query the cat api and filter by category. v1 of thecatapi does not allow querying with multiple categories, the api will return an empty array.
     *
     * @param n           the number of results to return
     * @param categoryIds Comma separated list of id's. As of now the cat api only allows one id.
     * @return A list of {@link CatImage}s.
     */
    @GET("/v1/images/search")
    fun searchByCategory(
        @Query("limit") n: Int,
        @Query("category_ids", encoded = true) breedIds: String
    ): Call<List<CatImage>>
}
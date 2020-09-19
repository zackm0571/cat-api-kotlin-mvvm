package com.zackmatthews.catapikotlinmvvm

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CatApi {

    @GET("/v1/breeds")
    fun getBreeds(): Call<List<CatBreed>>

    @GET("/v1/categories")
    fun getCategories(): Call<List<CatCategory>>

    @GET("/v1/images/search")
    fun getRandomImages(@Query("limit") n: Int): Call<List<CatImage>>

    @GET("/v1/images/search")
    fun searchByBreed(
        @Query("limit") n: Int,
        @Query("breed_ids", encoded = true) breedIds: String
    ): Call<List<CatImage>>

    @GET("/v1/images/search")
    fun searchByCategory(
        @Query("limit") n: Int,
        @Query("category_ids", encoded = true) breedIds: String
    ): Call<List<CatImage>>
}
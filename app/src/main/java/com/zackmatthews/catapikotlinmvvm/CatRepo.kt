package com.zackmatthews.catapikotlinmvvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CatRepo {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().client(OkHttpClient())
            .baseUrl(Constants.BASE_CAT_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private val catApi = retrofit.create(CatApi::class.java)

    val catImages: MutableLiveData<List<CatImage>> = MutableLiveData()
    val categories = MutableLiveData<List<CatCategory>>()
    val breeds = MutableLiveData<List<CatBreed>>()

    fun getCatImages(n: Int): MutableLiveData<List<CatImage>> {
        val results: MutableList<CatImage> = ArrayList()
        val apiCall = catApi.getRandomImages(n)

        apiCall.enqueue(object : Callback<List<CatImage>> {
            override fun onResponse(
                call: Call<List<CatImage>>,
                response: Response<List<CatImage>>
            ) {
                response.body()?.let {
                    results.addAll(response.body()!!)
                    Log.d(
                        javaClass.simpleName, String.format(
                            "getCatImaqes(%d), request: %s",
                            n,
                            call.request().toString()
                        )
                    )
                    Log.d(javaClass.simpleName, String.format("Results: %s", results.toString()))
                }
                catImages.postValue(results)
            }

            override fun onFailure(call: Call<List<CatImage>>, t: Throwable) {
                t.printStackTrace()
                Log.d(
                    javaClass.simpleName,
                    String.format("getCatImages(%d) failed. Info: %s", n, call.request().toString())
                )
            }
        })
        return catImages
    }

    /**
     * Retrieves n [CatImage]s filtered by Breed or Category.
     * Currently thecatapi returns an empty array when trying to search
     * with more than one category OR breed. Note: given more time I would split up
     * this api call.
     *
     * @param n             number of desired results
     * @param catBreeds     list of [CatBreed]
     * @param catCategories list of [CatCategory]
     * @return list of n [CatImage]s
     */
    fun getCatImagesFromSearchFilters(
        n: Int,
        catBreeds: List<CatBreed>,
        catCategories: List<CatCategory>
    ): MutableLiveData<List<CatImage>> {
        val results = ArrayList<CatImage>()
        val breedIds = StringBuilder("")
        val categoryIds = StringBuilder("")
        for (i in catBreeds.indices) {
            breedIds.append(catBreeds[i].id)
            if (i != catBreeds.size - 1) {
                breedIds.append(",")
            }
        }
        for (j in catCategories.indices) {
            categoryIds.append(catCategories[j].id)
            if (j != catCategories.size - 1) {
                categoryIds.append(",")
            }
        }
        var call: Call<List<CatImage>>? = null
        if (catBreeds.isNotEmpty()) {
            call = catApi.searchByBreed(
                n,
                breedIds.toString()
            )
        } else {
            call = catApi.searchByCategory(n, categoryIds.toString())
            Log.d(
                javaClass.simpleName,
                String.format(
                    "getCatImaqesFromSearchFilters(%d), request: %s",
                    n,
                    call.request().toString()
                )
            )
        }
        call.enqueue(object : Callback<List<CatImage>?> {
            override fun onResponse(
                call: Call<List<CatImage>?>,
                response: Response<List<CatImage>?>
            ) {
                response.body().let {
                    results.addAll(response.body()!!)
                    Log.d(
                        javaClass.simpleName,
                        String.format("Results: %s", results.toString())
                    )
                }

                catImages.postValue(results)
            }

            override fun onFailure(call: Call<List<CatImage>?>, t: Throwable) {
                t.printStackTrace()
                Log.d(
                    javaClass.simpleName,
                    String.format(
                        "getCatImagesFromSearchFilters(%d) failed. Info: %s",
                        n,
                        call.request().toString()
                    )
                )
            }
        })
        return catImages
    }


    fun fetchBreeds(): MutableLiveData<List<CatBreed>> {
        val results: MutableList<CatBreed> = ArrayList()
        val apiCall: Call<List<CatBreed>> = catApi.getBreeds()
        apiCall.enqueue(object : Callback<List<CatBreed>> {
            override fun onResponse(
                call: Call<List<CatBreed>>,
                response: Response<List<CatBreed>>
            ) {
                response.body()?.let {
                    results.addAll(response.body()!!)
                    Log.d(
                        javaClass.simpleName,
                        String.format("Results: %s", results.toString())
                    )
                }
                breeds.postValue(results)
            }

            override fun onFailure(call: Call<List<CatBreed>>, t: Throwable) {
                t.printStackTrace()
                Log.d(
                    javaClass.simpleName,
                    String.format("getBreeds() failed, request: %s", call.request().toString())
                )
            }

        })
        return breeds
    }

    fun fetchCategories(): MutableLiveData<List<CatCategory>> {
        val results: MutableList<CatCategory> = ArrayList()
        val apiCall: Call<List<CatCategory>> = catApi.getCategories()
        apiCall.enqueue(object : Callback<List<CatCategory>> {
            override fun onResponse(
                call: Call<List<CatCategory>>,
                response: Response<List<CatCategory>>
            ) {
                response.body()?.let {
                    results.addAll(response.body()!!)
                    Log.d(
                        javaClass.simpleName,
                        String.format("Results: %s", results.toString())
                    )
                }
                categories.postValue(results)
            }

            override fun onFailure(call: Call<List<CatCategory>>, t: Throwable) {
                t.printStackTrace()
                Log.d(
                    javaClass.simpleName,
                    String.format("getBreeds() failed, request: %s", call.request().toString())
                )
            }

        })
        return categories
    }
}
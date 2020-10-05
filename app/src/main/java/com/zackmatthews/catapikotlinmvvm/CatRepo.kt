package com.zackmatthews.catapikotlinmvvm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.collections.ArrayList

class CatRepo {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder().client(OkHttpClient())
            .baseUrl(Constants.BASE_CAT_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }
    private val catApi = retrofit.create(CatApi::class.java)
    
    val catImages: MutableLiveData<List<CatImage>> = MutableLiveData()
    val categories = MutableLiveData<List<CatCategory>>()
    val breeds = MutableLiveData<List<CatBreed>>()

    fun getCatImages(n: Int): MutableLiveData<List<CatImage>> {
        val results: MutableList<CatImage> = ArrayList()
        val observable = catApi.getRandomImages(n)

        CompositeDisposable().add(
            observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    it?.let {
                        results.addAll(it)
                        Log.d(
                            javaClass.simpleName, String.format(
                                "getCatImaqes(%d), response: %s",
                                n,
                                it.toString()
                            )
                        )
                        Log.d(
                            javaClass.simpleName,
                            String.format("Results: %s", results.toString())
                        )
                    }
                    catImages.postValue(results)

                }, {
                    it.printStackTrace()
                    Log.d(
                        javaClass.simpleName,
                        String.format(
                            "getCatImages(%d) failed. Info: %s"
                        )
                    )
                })
        )
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
        val disposable = CompositeDisposable()
        val observable = if (catBreeds.isNotEmpty())
            catApi.searchByBreed(n, breedIds.toString())
        else catApi.searchByCategory(n, categoryIds.toString())

        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    results.addAll(it)
                    Log.d(
                        javaClass.simpleName,
                        String.format("Results: %s", it.toString())
                    )
                    catImages.postValue(results)
                },
                    {
                        it.printStackTrace()
                        Log.d(
                            javaClass.simpleName,
                            "getCatImagesFromSearchFilters() error ${it.localizedMessage}"
                        )
                    })
        )
        return catImages
    }


    fun fetchBreeds(): MutableLiveData<List<CatBreed>> {
        val results: MutableList<CatBreed> = ArrayList()
        val disposable = CompositeDisposable()
        val observable: Observable<List<CatBreed>> = catApi.getBreeds()

        disposable.add(
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        results.addAll(it)
                        Log.d(
                            javaClass.simpleName,
                            String.format("Results: %s", results.toString())
                        )
                        breeds.postValue(results)
                    },
                    {
                        // todo: Load cache
                        it.printStackTrace()
                        Log.d(
                            javaClass.simpleName,
                            "getBreeds() failed, request: %s"
                        )
                    })
        )
        return breeds
    }

    fun fetchCategories(): MutableLiveData<List<CatCategory>> {
        val results: MutableList<CatCategory> = ArrayList()
        val disposable = CompositeDisposable()
        val observable = catApi.getCategories()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                    {
                        results.addAll(it)
                        Log.d(
                            javaClass.simpleName,
                            String.format("Results: %s", results.toString())
                        )
                        categories.postValue(results)

                    },
                    {
                        it.printStackTrace()
                        Log.d(
                            javaClass.simpleName,
                            "getBreeds() failed"
                        )
                    })
        )

        return categories
    }
}
package com.zackmatthews.catapikotlinmvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val catRepo: CatRepo = CatRepo()
    private var catImages: MutableLiveData<List<CatImage>> = MutableLiveData()
    private var breeds: MutableLiveData<List<CatBreed>> = catRepo.fetchBreeds()
    private var categories: MutableLiveData<List<CatCategory>> = catRepo.fetchCategories()

    fun getRandomCatImages(): MutableLiveData<List<CatImage>> {
        catImages = catRepo.getCatImages(Constants.DEFAULT_IMAGES_TO_LOAD)
        return catImages
    }

    /**
     * Retrieves n [CatImage]s filtered by Breed or Category.
     * Currently thecatapi returns an empty array when trying to search
     * with more than one category OR breed. The number of results is
     * currently defined by [Constants.DEFAULT_IMAGES_TO_LOAD] which is 10.
     * Note: given more time I would split up this api call.
     *
     * @param catBreeds     list of [CatBreed]
     * @param catCategories list of [CatCategory]
     * @return list of n [CatImage]s
     */
    fun getImagesFromSearchFilter(
        catBreeds: List<CatBreed>,
        catCategories: List<CatCategory>
    ): MutableLiveData<List<CatImage>> {
        return catRepo.getCatImagesFromSearchFilters(
            Constants.DEFAULT_IMAGES_TO_LOAD,
            catBreeds,
            catCategories
        )
    }

    fun fetchBreeds(): MutableLiveData<List<CatBreed>> {
        breeds = catRepo.fetchBreeds()
        return breeds
    }

    fun fetchCategories(): MutableLiveData<List<CatCategory>> {
        categories = catRepo.fetchCategories()
        return categories
    }
}
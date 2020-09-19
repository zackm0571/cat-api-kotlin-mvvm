package com.zackmatthews.catapikotlinmvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val catRepo: CatRepo = CatRepo()
    private var catImages: MutableLiveData<List<CatImage>> = MutableLiveData()
    val breeds: MutableLiveData<List<CatBreed>> = catRepo.fetchBreeds()
    val categories: MutableLiveData<List<CatCategory>> = catRepo.fetchCategories()

    fun getRandomCatImages() : MutableLiveData<List<CatImage>>{
        catImages = catRepo.getCatImages(Constants.DEFAULT_IMAGES_TO_LOAD)
        return catImages
    }
}
package com.zackmatthews.catapikotlinmvvm

class CatImage {
    var id: String? = null
    var url: String? = null
    var categories: List<CatCategory>? = null
    var breeds: List<CatBreed>? = null


    override fun toString(): String {
        return String.format(
            "id: %s\nurl: %s\ncategories: %s\nbreeds: %s",
            id,
            url,
            if (categories != null) categories.toString() else null,
            if (breeds != null) breeds.toString() else null
        )
    }
}

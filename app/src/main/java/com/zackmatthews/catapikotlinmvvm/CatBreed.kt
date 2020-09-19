package com.zackmatthews.catapikotlinmvvm

import com.google.gson.annotations.SerializedName

class CatBreed {
    lateinit var id: String
    lateinit var name: String

    @SerializedName("life_span")
    lateinit var lifespan: String
    lateinit var origin: String

    @SerializedName("wikipedia_url")
    lateinit var wikipediaUrl: String

    override fun toString(): String {
        return String.format(
            "id: %s, name: %s, " +
                    "lifespan: %s, origin: %s, wikipediaUrl: %s",
            id, name, lifespan, origin, wikipediaUrl
        )
    }
}

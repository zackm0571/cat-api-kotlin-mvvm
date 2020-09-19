package com.zackmatthews.catapikotlinmvvm

class CatCategory {

    var id: Int = 0
    lateinit var name: String

    override fun toString(): String {
        return String.format("id: %s, name:%s", id, name)
    }
}

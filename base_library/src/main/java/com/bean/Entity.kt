package com.bean

import com.google.gson.Gson

open class Entity : UnProguard {

    override fun toString(): String {
        return GSON.toJson(this)
    }

    companion object {

        private val GSON = Gson()
    }
}
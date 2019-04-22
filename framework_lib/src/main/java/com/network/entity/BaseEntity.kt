package com.network.entity

open class BaseEntity<T> {

    var code: Int? = null
    var message: String? = null
    var data: T? = null

}
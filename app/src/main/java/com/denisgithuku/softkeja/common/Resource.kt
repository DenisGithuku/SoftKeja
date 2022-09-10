package com.denisgithuku.softkeja.common

sealed class Resource<out T>(val data: T? = null, val error: String = "") {
    class Loading<out T> : Resource<T>()
    class Success<out T>(data: T?): Resource<T>(data)
    class Error<out T>(error: String): Resource<T>(null, error)
}
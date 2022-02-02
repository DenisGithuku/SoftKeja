package com.denisgithuku.softkeja.util

sealed class Resource<out T>(val data: T? = null, val error: String? = "") {
    class Loading<T> : Resource<T>()
    class Success<T>(data: T?): Resource<T>(data, null)
    class Error<T>(error: String): Resource<T>(null, error)
}
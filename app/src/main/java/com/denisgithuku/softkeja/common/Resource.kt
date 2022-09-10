package com.denisgithuku.softkeja.common

sealed class Resource<out T>(val data: T? = null, val error: Throwable? = null) {
    class Loading<out T> : Resource<T>()
    class Success<out T>(data: T?): Resource<T>(data)
    class Error<out T>(error: Throwable): Resource<T>(null, error)
}

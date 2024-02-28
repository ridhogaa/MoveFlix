package com.ergea.moveflix.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlin.Exception


sealed class Resource<T>(
    val payload: T? = null,
    val message: String? = null,
    val exception: Exception? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(exception: Exception?, data: T? = null) :
        Resource<T>(data, exception = exception)

    class Empty<T>(data: T? = null) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}

fun <T> Resource<T>.proceedWhen(
    doOnSuccess: ((resource: Resource<T>) -> Unit)? = null,
    doOnError: ((resource: Resource<T>) -> Unit)? = null,
    doOnLoading: ((resource: Resource<T>) -> Unit)? = null,
    doOnEmpty: ((resource: Resource<T>) -> Unit)? = null
) {
    when (this) {
        is Resource.Success -> doOnSuccess?.invoke(this)
        is Resource.Error -> doOnError?.invoke(this)
        is Resource.Loading -> doOnLoading?.invoke(this)
        is Resource.Empty -> doOnEmpty?.invoke(this)
    }
}

fun <T> proceedFlow(block: suspend () -> T): Flow<Resource<T>> {
    return flow<Resource<T>> {
        val result = block.invoke()
        emit(
            if (result is Collection<*> && result.isEmpty()) {
                Resource.Empty(result)
            } else {
                Resource.Success(result)
            }
        )
    }.catch { e ->
        emit(Resource.Error(exception = Exception(e)))
    }.onStart {
        emit(Resource.Loading())
    }
}

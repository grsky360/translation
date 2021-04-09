package ilio.translation.utils

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun <T> async(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> T
): Deferred<T> = GlobalScope.async(context, start, block)

fun <T> await(deferred: Deferred<T>): T? {
    return runBlocking {
        deferred.await()
    }
}

fun <T> await(block: () -> Deferred<T>): T? {
    val deferred = block()
    return runBlocking {
        deferred.await()
    }
}

fun <T> Deferred<T>.onSuccess(block: Deferred<T>.(T?) -> Unit): Deferred<T> {
    this.invokeOnCompletion { throwable ->
        if (throwable == null) {
            block(getCompleted())
        }
    }
    return this
}

fun <T> Deferred<T>.onError(block: Deferred<T>.(Throwable) -> Unit): Deferred<T> {
    this.invokeOnCompletion { throwable ->
        if (throwable != null) {
            block(throwable)
        }
    }
    return this
}

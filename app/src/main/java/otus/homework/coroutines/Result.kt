package otus.homework.coroutines

sealed class Result<out T : Any> {
    data class Success<out T : Any> (val result: T): Result<T>()
    data class ErrorResult (val error: Throwable): Result<Nothing>()
}


package org.app.data.model

sealed class ResponseData<out T> {

  class Success<out T>(val value: T) : ResponseData<T>()

  class Failure(
    val failureStatus: FailureStatus,
    val code: Int? = null,
    val message: String? = null
  ) : ResponseData<Nothing>()

}
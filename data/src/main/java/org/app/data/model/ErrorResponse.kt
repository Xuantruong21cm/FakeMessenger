package org.app.data.model

data class ErrorResponse(
  val detail: String,
  val instance: Any,
  val status: Int,
  val title: String,
  val type: String
)
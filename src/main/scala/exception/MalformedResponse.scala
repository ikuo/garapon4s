package com.github.ikuo.garapon4s

class MalformedResponse(message: String, throwable: Throwable = null)
  extends RuntimeException(message, throwable)

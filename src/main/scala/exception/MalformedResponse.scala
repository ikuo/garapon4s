package com.github.ikuo.garapon4s

/**
 * Malformed response from TV device.
 */
class MalformedResponse(message: String, throwable: Throwable = null)
  extends RuntimeException(message, throwable)

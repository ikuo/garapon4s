package com.github.ikuo.garapon4s

import com.fasterxml.jackson.core.{JsonLocation, JsonToken, JsonParser}

class MalformedJsonResponse(
  val message: String,
  val parser: JsonParser,
  val cause: Throwable = null
) extends RuntimeException(s"${message}, ${parser.getCurrentLocation}", cause)
{
  def this(expectedToken: JsonToken, parser: JsonParser) =
    this(s"Expecting ${expectedToken.toString}.", parser)
}

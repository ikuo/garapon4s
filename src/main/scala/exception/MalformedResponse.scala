package com.github.ikuo.garapon4s

import com.fasterxml.jackson.core.{JsonLocation, JsonToken, JsonParser}

class MalformedResponse(
  val message: String,
  val parser: JsonParser
) extends RuntimeException(s"${message}, ${parser.getCurrentLocation}")
{
  def this(expectedToken: JsonToken, parser: JsonParser) =
    this(s"Expecting ${expectedToken.toString}.", parser)
}

package com.github.ikuo.garapon4s

import java.io.InputStream
import com.fasterxml.jackson.core.{JsonFactory, JsonParser, JsonToken}
import com.fasterxml.jackson.databind.ObjectMapper

trait PullParsing {
  protected val NA = -1
  protected lazy val objectMapper = new ObjectMapper
  protected lazy val jsonFactory = new JsonFactory

  protected def acceptToken(token: JsonToken, parser: JsonParser) {
    if (parser.isClosed || parser.nextToken != token)
      throw new MalformedJsonResponse(token, parser)
  }

  protected def intFieldValue(parser: JsonParser): Int = {
    val intValue = parser.nextIntValue(NA)
    if (intValue != NA) return intValue

    val value = parser.getText
    if (value == null || value.length < 1) sys.error("Empty string for integer.")
    else value.toInt
  }

  protected def unexpectedField(name: String, parser: JsonParser) =
    throw new MalformedJsonResponse(s"Unexpected field name ${name}", parser)

  protected def unexpected(token: JsonToken, parser: JsonParser) =
    throw new MalformedJsonResponse(s"Unexpected token ${token}", parser)
}

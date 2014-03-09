package com.github.ikuo.garapon4s.model

import java.io.InputStream
import com.fasterxml.jackson.core.{JsonFactory, JsonParser, JsonToken}
import com.github.ikuo.garapon4s.MalformedResponse

class SearchResult(
  val status: Int,
  val hit: Int,
  val version: String,
  val programs: Stream[Program]
) extends Validation {
   //(new ObjectMapper).readValue(response.body.inputStream, classOf[AuthResult])
}

object SearchResult {
  import JsonToken._

  private lazy val jsonFactory = new JsonFactory
  private val INVALID = -1

  def apply(in: InputStream) = {
    val parser = jsonFactory.createParser(in)
    var values = Tuple3[Int, Int, String](INVALID,INVALID,null)

    try {
      acceptToken(START_OBJECT, parser)

      while(!parser.isClosed) {
        parser.nextToken match {
          case FIELD_NAME => parser.getText match {
            case "status" => values = values.copy(_1 = intFieldValue(parser))
            case "hit" => values = values.copy(_2 = intFieldValue(parser))
            case "program" => ()//values = values.copy(_3 = parser.nextTextValue)
            case "version" => values = values.copy(_3 = parser.nextTextValue)
            case token => ()//println(token)
          }
          case _ => ()
        }
      }
      println(values)
      //TODO: build SearchResult
    } catch {
      case error: RuntimeException => throw new MalformedResponse(error.getMessage, parser)
      case error: Throwable => throw error
    }
    new SearchResult(0,0,"", null)
  }

  private def searchResults(parser: JsonParser): Stream[SearchResult] =
    if (parser.isClosed) Stream.empty
    else {
      Stream.empty
    }

  private def intFieldValue(parser: JsonParser): Int =
    parser.nextIntValue(INVALID) match {
      case INVALID => parser.getText match {
        case value if (value == null || value.length < 1) =>
          sys.error("Empty string for integer.")
        case value => value.toInt
      }
      case value => value
    }

  protected def acceptToken(token: JsonToken, parser: JsonParser) {
    if (parser.isClosed || parser.nextToken != token)
      throw new MalformedResponse(token, parser)
  }
}

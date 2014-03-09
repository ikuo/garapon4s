package com.github.ikuo.garapon4s.model

import java.io.InputStream
import com.fasterxml.jackson.core.{JsonFactory, JsonParser, JsonToken}
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ikuo.garapon4s.MalformedResponse

class SearchResult(
  val status: Int,
  val hit: Int,
  val version: String,
  val programs: List[Program]
) extends Validation {
}

object SearchResult {
  import JsonToken._

  private lazy val jsonFactory = new JsonFactory
  private lazy val objectMapper = new ObjectMapper
  private val NA = -1

  def parse(
    in: InputStream,
    resultListener: SearchResultListener = null
  ): SearchResult = {
    val parser = jsonFactory.createParser(in)
    val listener = Option(resultListener).getOrElse(new BufferedListener)

    try {
      acceptToken(START_OBJECT, parser)
      while(!parser.isClosed) {
        parser.nextToken match {
          case FIELD_NAME => parser.getText match {
            case "status"   => listener.notifyStatus(intFieldValue(parser))
            case "hit"      => listener.notifyHit(intFieldValue(parser))
            case "version"  => listener.notifyVersion(parser.nextTextValue)
            case "program"  => {
              acceptToken(START_ARRAY, parser)
              listener.notifyPrograms(parsePrograms(parser))
            }
            case name => unexpectedField(name, parser)
          }
          case END_OBJECT | null => ()
          case token => unexpected(token, parser)
        }
      }
    } catch {
      case error: MalformedResponse => throw error
      case error: RuntimeException =>
        throw new MalformedResponse(error.getMessage, parser, error)
      case error: Throwable => throw error
    }

    listener.getResult
  }

  protected def parsePrograms(parser: JsonParser): Stream[Program] =
    if (parser.nextToken == END_ARRAY || parser.isClosed) { Stream.empty }
    else {
      val program = objectMapper.readValue(parser, classOf[Program])
      program #:: parsePrograms(parser)
    }

  protected def unexpected(token: JsonToken, parser: JsonParser) =
    throw new MalformedResponse(s"Unexpected token ${token}", parser)

  protected def unexpectedField(name: String, parser: JsonParser) =
    throw new MalformedResponse(s"Unexpected field name ${name}", parser)

  class BufferedListener extends SearchResultListener {
    protected var status: Int = NA
    protected var hit: Int = NA
    protected var version: String = null
    protected var programs: List[Program] = null
    override def notifyStatus(value: Int) { this.status = value }
    override def notifyHit(value: Int) { this.hit = value }
    override def notifyVersion(value: String) { this.version = value }
    override def notifyPrograms(programs: Stream[Program]) { this.programs = programs.toList }
    override def getResult: SearchResult = new SearchResult(status, hit, version, programs)
  }

  private def searchResults(parser: JsonParser): Stream[SearchResult] =
    if (parser.isClosed) Stream.empty
    else {
      Stream.empty
    }

  private def intFieldValue(parser: JsonParser): Int = {
    val intValue = parser.nextIntValue(NA)
    if (intValue != NA) return intValue

    val value = parser.getText
    if (value == null || value.length < 1) sys.error("Empty string for integer.")
    else value.toInt
  }

  protected def acceptToken(token: JsonToken, parser: JsonParser) {
    if (parser.isClosed || parser.nextToken != token)
      throw new MalformedResponse(token, parser)
  }
}

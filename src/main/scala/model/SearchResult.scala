package com.github.ikuo.garapon4s.model

import java.io.InputStream
import com.fasterxml.jackson.core.{JsonFactory, JsonParser, JsonToken}
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ikuo.garapon4s.MalformedJsonResponse

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
    val parser = jsonFactory.createJsonParser(in)
    val listener = Option(resultListener).getOrElse(new BufferedListener)

    acceptToken(START_OBJECT, parser)
    while(!parser.isClosed) {
      parser.nextToken match {
        case FIELD_NAME => parser.getText match {
          case "status"   => listener.notifyStatus(intFieldValue(parser))
          case "hit"      => listener.notifyHit(intFieldValue(parser))
          case "version"  => listener.notifyVersion(parser.nextTextValue)
          case "program"  => {
            val iterator = new ProgramIterator(parser)
            listener.notifyPrograms(iterator)
            if (iterator.hasNext) {
              throw new IllegalStateException(
                "Program iterator is not completely consumed during the call to notifyPrograms()")
            }
          }
          case name => unexpectedField(name, parser)
        }
        case END_OBJECT | null => ()
        case token => unexpected(token, parser)
      }
    }

    listener.getResult
  }

  class ProgramIterator(val parser: JsonParser) extends Iterator[Program] {
    acceptToken(START_ARRAY, parser)
    private var _next: Program = parseProgram

    override def next: Program = {
      val result = _next
      _next = parseProgram
      result
    }

    override def hasNext: Boolean = (_next != null)

    protected def parseProgram: Program =
      if (parser.nextToken == END_ARRAY || parser.isClosed) { null }
      else objectMapper.readValue(parser, classOf[Program])
  }

  protected def unexpected(token: JsonToken, parser: JsonParser) =
    throw new MalformedJsonResponse(s"Unexpected token ${token}", parser)

  protected def unexpectedField(name: String, parser: JsonParser) =
    throw new MalformedJsonResponse(s"Unexpected field name ${name}", parser)

  class BufferedListener extends SearchResultListener {
    protected var status: Int = NA
    protected var hit: Int = NA
    protected var version: String = null
    protected var programs: List[Program] = null
    override def notifyStatus(value: Int) { this.status = value }
    override def notifyHit(value: Int) { this.hit = value }
    override def notifyVersion(value: String) { this.version = value }
    override def notifyPrograms(programs: Iterator[Program]) { this.programs = programs.toList }
    override def getResult: SearchResult = new SearchResult(status, hit, version, programs)
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
      throw new MalformedJsonResponse(token, parser)
  }
}

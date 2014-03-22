package com.github.ikuo.garapon4s.model

import java.io.InputStream
import com.fasterxml.jackson.core.{JsonFactory, JsonParser, JsonToken}
import com.github.ikuo.garapon4s.{PullParsing, MalformedJsonResponse}
import JsonToken._

class SearchResult(
  val status: Int,
  val hit: Int,
  val version: String,
  val programs: List[Program]
) extends Validation {
}

object SearchResult extends PullParsing {
  def parse(
    in: InputStream,
    resultListener: SearchResultListener = null
  ): SearchResult = {
    val parser = jsonFactory.createParser(in)
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
}

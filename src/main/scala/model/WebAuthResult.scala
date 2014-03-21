package com.github.ikuo.garapon4s.model

import collection.mutable.HashMap
import java.util.NoSuchElementException

class WebAuthResult(
  val ipAddress: String,
  val privateIpAddress: String,
  val globalIpAddress: String,
  val portHttp: Int,
  val portTs: Int,
  val gtvVersion: String
) {
}

object WebAuthResult {
  class WrongPassword(string: String) extends RuntimeException(string)
  class UnknownUser(string: String) extends RuntimeException(string)
  class UnknownRegistKey(string: String) extends RuntimeException(string)
  class UnknownIpAddress(string: String) extends RuntimeException(string)
  class UnknownDeveloper(string: String) extends RuntimeException(string)
  class MalformedResponse(string: String, throwable: Throwable = null)
    extends RuntimeException(string, throwable)

  def parse(string: String): WebAuthResult = {
    if (string == null) throw new MalformedResponse(string)
    val lines = string.lines.toArray
    if (lines.size < 1) throw new MalformedResponse(string)

    val (code, message) =
      lines(0).split(";") match {
        case Array(code, msg) => (code.toInt, msg.trim)
        case _ => throw new MalformedResponse(string)
      }

    if (code == 0) return parseSuccessContent(lines.drop(1))
    if (code != 1) throw new MalformedResponse(string)

    message match {
      case "wrong	password"     => throw new WrongPassword(message)
      case "unknown user"       => throw new UnknownUser(message)
      case "unknown registkey"  => throw new UnknownRegistKey(message)
      case "unknown ip address" => throw new UnknownRegistKey(message)
      case "unknown	developer"  => throw new UnknownDeveloper(message)
      case _                    => throw new MalformedResponse(string)
    }
  }

  private def parseSuccessContent(lines: Array[String]) = {
    val map = HashMap[String, String]()
    for (line <- lines) {
      line.split(";") match {
        case Array(k, v) => map.update(k, v)
        case _ => throw new MalformedResponse(s"Unexpected line: '${line}'")
      }
    }

    try {
      new WebAuthResult(
        ipAddress = map("ipaddr"),
        privateIpAddress = map("pipaddr"),
        globalIpAddress = map("gipaddr"),
        portHttp = map("port").toInt,
        portTs = map("port2").toInt,
        gtvVersion = map("gtvver")
      )
    } catch {
      case err: NoSuchElementException =>
        throw new MalformedResponse(lines.mkString("\n"), err)
    }
  }
}

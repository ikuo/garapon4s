package com.github.ikuo.garapon4s.model

import com.github.ikuo.garapon4s.{InvalidSession, ParameterError}
import FavoriteResult._

class FavoriteResult(
  var status: Int,
  var version: String
) {
  def setStatus(v: Int) { this.status = v }
  def setVersion(v: String) { this.version = v }

  def validate {
    status match {
      case 1 => ()
      case 0 => throw new InvalidSession
      case 100 => throw new ParameterError
      case 150 => throw new NoMp4Error
      case 200 => throw new DbConnectionError
      case _ => throw new MalformedResponse(s"status = ${status}")
    }
  }
}

object FavoriteResult {
  class NoMp4Error extends RuntimeException
  class DbConnectionError extends RuntimeException
}

package com.github.ikuo.garapon4s.model

import com.github.ikuo.garapon4s.{
  ParameterError, MalformedResponse, UnknownUser, WrongPassword}
import AuthResult._

/** Result of /login API. */
class AuthResult(
  var version: String,
  var status: Int,
  var login: Int,
  var gtvsession: String
) extends Validation {
  def this() = { this(null, -1, -1, null) }
  def setVersion(a: String) { this.version = a }
  def setStatus(a: Int) { this.status = a }
  def setLogin(a: Int) { this.login = a }
  def setGtvsession(a: String) { this.gtvsession = a }

  def validated = {
    status match {
      case 1 => ()
      case 100 => throw new ParameterError()
      case 200 => throw new AuthSyncError()
      case _ => throw new MalformedResponse(s"status = ${status}")
    }

    login match {
      case 1 => ()
      case 0 => throw new LoginError()
      case 100 => throw new UnknownUser("by login = 100")
      case 200 => throw new WrongPassword("by login = 200")
      case _ => throw new MalformedResponse(s"login = ${login}")
    }

    nonEmpty("version", version)
    nonEmpty("gtvsession", gtvsession)

    this
  }
}

object AuthResult {
  class LoginError extends RuntimeException("Error status or empty parameter.")
  class AuthSyncError
    extends RuntimeException("Make sure the internet connection and wait about 5 minutes.")
}

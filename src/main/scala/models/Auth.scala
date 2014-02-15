package com.github.ikuo.garapon4s

class Auth(
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

  def validated: Auth = {
    nonEmpty("version", version)
    nonEmpty("gtvsession", gtvsession)
    nonNegative("status", status)
    nonNegative("login", login)
    //TODO fine grain validation
    this
  }
}

package com.github.ikuo.garapon4s

import java.net.URL

/**
 * Unknown user.
 */
class UnknownUser(message: String) extends RuntimeException(message)

/**
 * Wrong password.
 */
class WrongPassword(message: String) extends RuntimeException(message)

/**
 * "Error-status" or empty parameter.
 */
class LoginError extends RuntimeException("Error-status or empty parameter.")

/**
 * "Error-status" or empty parameter.
 */
class LogoutError extends RuntimeException("Error-status or empty parameter.")

/**
 * TV device is not synchronized to the central server.
 */
class AuthSyncError
  extends RuntimeException("Make sure the internet connection and wait about 5 minutes.")

/**
 * "Parameter error" in the API spec.
 */
class ParameterError extends RuntimeException

case class ConnectionFailure(url: URL, cause: Throwable)
  extends RuntimeException(s"Failed to connect to ${url}", cause)

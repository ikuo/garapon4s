package com.github.ikuo.garapon4s

import java.net.{URL, InetAddress}
import java.security.MessageDigest
import java.math.BigInteger

import uk.co.bigbeeconsultants.http.HttpClient
import uk.co.bigbeeconsultants.http.request.RequestBody

class TvClient(
  devId: String,
  httpClientFactory: HttpClientFactory = TvClient.defaultHttpClientFactory
) {
  val endpointUrl = new URL("http://garagw.garapon.info/getgtvaddress")

  def newSession(
    user: String,
    password: String,
    md5Converted: Boolean = true
  ): TvSession = {
    val response =
      httpClientFactory.create.post(
        endpointUrl,
        Some(RequestBody(Map(
          "user" -> user,
          "md5passwd" -> md5Password(password, md5Converted),
          "dev_id" -> devId
        )))
      )
    println(response.body)
    new TvSession()
  }

  def newSessionViaLAN(
    inetAddress: InetAddress,
    user: String,
    password: String,
    md5Converted: Boolean = true
  ): TvSession = {
    val url = s"http://${inetAddress.getHostAddress}/gapi/v3/auth"
    println(url)
    val response =
      httpClientFactory.create.post(
        new URL(url),
        Some(RequestBody(Map(
          "type" -> "login",
          "loginid" -> user,
          "md5pswd" -> md5Password(password, md5Converted)
        )))
      )
    println(response.body)
    new TvSession()
  }

  private def md5Password(password: String, md5Converted: Boolean) =
    if (md5Converted) password else md5sum(password)

  private def md5sum(text: String) = {
    val bytes = MessageDigest.getInstance("MD5").digest(text.getBytes())
	  val hexString = (new BigInteger(1, bytes)).toString(16)
    ("0" * (32 - hexString.size)) + hexString // pad "0" chars to left
  }
}

object TvClient {
  lazy val defaultHttpClientFactory =
    new HttpClientFactory {
      override def create: HttpClient = { new HttpClient }
    }
}

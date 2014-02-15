package com.github.ikuo.garapon4s

import java.security.MessageDigest
import java.math.BigInteger

import uk.co.bigbeeconsultants.http.HttpClient
import uk.co.bigbeeconsultants.http.request.RequestBody

class TvClient(
  val user: String,
  md5Password: String,
  devId: String,
  httpClientFactory: HttpClientFactory
) {
  val endpointUrl = new java.net.URL("http://garagw.garapon.info/getgtvaddress")

  def newSession: TvSession = {
    val httpClient = httpClientFactory.create
    println(md5Password)
    val response =
      httpClient.post(
        endpointUrl,
        Some(RequestBody(Map(
          "user" -> user,
          "md5passwd" -> md5Password,
          "dev_id" -> devId
        )))
      )
    println(response.body)
    new TvSession()
  }
}

object TvClient {
  def fromRawPassword(
    user: String,
    rawPassword: String,
    devId: String,
    factory: HttpClientFactory = defaultHttpClientFactory
  ) = new TvClient(user, md5sum(rawPassword), devId, factory)

  lazy val defaultHttpClientFactory =
    new HttpClientFactory {
      override def create: HttpClient = { new HttpClient }
    }

  private def md5sum(text: String) = {
    val bytes = MessageDigest.getInstance("MD5").digest(text.getBytes())
	  val hexString = (new BigInteger(1, bytes)).toString(16)
    ("0" * (32 - hexString.size)) + hexString // pad "0" chars to left
  }
}

package com.github.ikuo.garapon4s

import java.net.URL
import java.security.MessageDigest
import java.math.BigInteger

import uk.co.bigbeeconsultants.http.{HttpClient, Config}
import uk.co.bigbeeconsultants.http.request.RequestBody

import com.fasterxml.jackson.databind.ObjectMapper

import com.github.ikuo.garapon4s.model.AuthResult

/**
 * A client of TV device.
 */
class TvClient(
  devId: String,
  httpClientFactory: HttpClientFactory =
    HttpClientFactory({
      new HttpClient(Config(connectTimeout = 5000, readTimeout = 15000))
    })
) {
  val endpointUrl = new URL("http://garagw.garapon.info/getgtvaddress")

  def newSessionByIp(ip: String, user: String, md5Password: String): TvSession = {
    val auth = {
      val url = s"http://${ip}/gapi/v3/auth?dev_id=${devId}"
      val body = Some(RequestBody(Map(
        "type" -> "login",
        "loginid" -> user,
        "md5pswd" -> md5Password
      )))
      val response =
        httpClientFactory.create.post(new URL(url), body, Nil)
      (new ObjectMapper).readValue(response.body.inputStream, classOf[AuthResult]).
        validated
    }

    new TvSession(ip, auth.gtvsession, devId, httpClientFactory)
  }

  def newSession(user: String, md5Password: String): TvSession = {
    val response =
      httpClientFactory.create.post(
        endpointUrl,
        Some(RequestBody(Map(
          "user" -> user,
          "md5passwd" -> md5Password,
          "dev_id" -> devId
        )))
      )
    //TODO: parse result and login to the TV
    //new TvSession(ip, gtvsession, devId)
    null
  }

  def md5sum(text: String) = {
    val bytes = MessageDigest.getInstance("MD5").digest(text.getBytes())
	  val hexString = (new BigInteger(1, bytes)).toString(16)
    ("0" * (32 - hexString.size)) + hexString // pad "0" chars to left
  }
}

package com.github.ikuo.garapon4s

import java.net.{URL, InetAddress}
import java.security.MessageDigest
import java.math.BigInteger

import uk.co.bigbeeconsultants.http.HttpClient
import uk.co.bigbeeconsultants.http.request.RequestBody

import com.fasterxml.jackson.databind.ObjectMapper

import com.github.ikuo.garapon4s.model.Auth

class TvClient(
  devId: String,
  httpClientFactory: HttpClientFactory = TvClient.defaultHttpClientFactory
) {
  val endpointUrl = new URL("http://garagw.garapon.info/getgtvaddress")

  def newSessionByIp(ip: String, user: String, md5Password: String): TvSession = {
    val auth = {
      val url = s"http://${ip}/gapi/v3/auth?${devId}"
      val response =
        httpClientFactory.create.post(
          new URL(url),
          Some(RequestBody(Map(
            "type" -> "login",
            "loginid" -> user,
            "md5pswd" -> md5Password
          )))
        )
      (new ObjectMapper).readValue(response.body.inputStream, classOf[Auth]).
        validated
    }

    new TvSession(ip, auth.gtvsession, devId)
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

object TvClient {
  lazy val defaultHttpClientFactory =
    new HttpClientFactory {
      override def create: HttpClient = { new HttpClient }
    }
}

package com.github.ikuo.garapon4s

import java.net.URL
import java.security.MessageDigest
import java.math.BigInteger
import java.net.InetAddress
import uk.co.bigbeeconsultants.http.{HttpClient, Config}
import uk.co.bigbeeconsultants.http.request.RequestBody
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.ikuo.garapon4s.model.AuthResult
import model.WebAuthResult

/**
 * A client of TV device.
 */
class TvClient(
  devId: String,
  httpClientFactory: HttpClientFactory =
    HttpClientFactory({
      new HttpClient(Config(connectTimeout = 5000, readTimeout = 15000))
    })
) extends IpCheck
{
  val endpointUrl = new URL("http://garagw.garapon.info/getgtvaddress")
  private lazy val objectMapper = new ObjectMapper

  /**
   * Create new session with specific IP address.
   * @param ip IP address
   * @param user user name
   * @param md5Password MD5 password
   * @param portHttp port number for HTTP
   * @param portTs port number for Ts
   */
  @throws(classOf[UnknownUser])
  @throws(classOf[WrongPassword])
  @throws(classOf[MalformedResponse])
  @throws(classOf[ParameterError])
  @throws(classOf[AuthSyncError])
  @throws(classOf[LoginError])
  def newSessionByIp(
    ip: String,
    user: String,
    md5Password: String,
    portHttp: Int = 80,
    portTs: Int = 1935
  ): TvSession = {
    val auth = {
      val url = s"http://${ip}:${portHttp}/gapi/v3/auth?dev_id=${devId}"
      val body = Some(RequestBody(Map(
        "type" -> "login",
        "loginid" -> user,
        "md5pswd" -> md5Password
      )))
      val response =
        httpClientFactory.create.post(new URL(url), body, Nil)
      objectMapper.readValue(response.body.inputStream, classOf[AuthResult]).
        validate()
    }

    new TvSession(ip, portHttp, portTs, auth.gtvsession, devId, httpClientFactory)
  }

  /**
   * Create new session.
   * @param user user name
   * @param md5Password MD5 password
   * @param timeoutMs timeout in milli seconds
   * @throws Unknownuser See [[UnknownUser]]
   * @throws WrongPassword See [[WrongPassword]]
   * @throws Malformedresponse See [[MalformedResponse]]
   * @throws ParameterError See [[ParameterError]]
   * @throws AuthSyncError See [[AuthSyncError]]
   * @throws LoginError See [[LoginError]]
   */
  @throws(classOf[UnknownUser])
  @throws(classOf[WrongPassword])
  @throws(classOf[MalformedResponse])
  @throws(classOf[ParameterError])
  @throws(classOf[AuthSyncError])
  @throws(classOf[LoginError])
  def newSession(
    user: String,
    md5Password: String,
    timeoutMs: Int = 2000
  ): TvSession = {
    val response =
      httpClientFactory.create.post(
        endpointUrl,
        Some(RequestBody(Map(
          "user" -> user,
          "md5passwd" -> md5Password,
          "dev_id" -> devId
        )))
      )

    val result = WebAuthResult.parse(response.body.toString)

    val candidates =
      List(result.ipAddress, result.privateIpAddress, result.globalIpAddress)

    val ip = candidates.find(InetAddress.getByName(_).isReachable(timeoutMs))
    if (ip.isEmpty) throw new UnreachableIp(candidates)

    val port = if (isPrivate(ip.get.toString)) 80 else result.portHttp

    newSessionByIp(ip.get.toString, user, md5Password, port, result.portTs)
  }

  /**
   * Get MD5 sum.
   * @param text original string
   */
  def md5sum(text: String) = {
    val bytes = MessageDigest.getInstance("MD5").digest(text.getBytes())
	  val hexString = (new BigInteger(1, bytes)).toString(16)
    ("0" * (32 - hexString.size)) + hexString // pad "0" chars to left
  }
}

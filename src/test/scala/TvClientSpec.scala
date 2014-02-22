package com.github.ikuo.garapon4s

import org.scalatest.FunSpec
import uk.co.bigbeeconsultants.http.HttpClient

import java.net.URL
import uk.co.bigbeeconsultants.http.request.RequestBody
import uk.co.bigbeeconsultants.http.header.Headers

class TvClientSpec extends UnitSpec {
  describe("#newSessionByIp") {
    it("should return a TvSession with valid ip, gtvsession, and devId") {
      val httpClient = mock[HttpClient]

      (httpClient.post(_: URL, _: Option[RequestBody], _: Headers)).
        expects(new URL("http://192.168.0.1/gapi/v3/auth?dev_id1"), *, *).
        returning(
          MockResponse.ofJson(
            """{"version":"GTV3.1401190","status":1,"login":1,"gtvsession":"a9cfaa236dda032a39a47a77a961dec9"}"""
          ))

      val tvClient = new TvClient("dev_id1", HttpClientFactory({ httpClient }))
      val session = tvClient.newSessionByIp("192.168.0.1", "user1", "md5pw1")

      session shouldBe a [TvSession]
      session.ip should be ("192.168.0.1")
      session.gtvsession should be ("a9cfaa236dda032a39a47a77a961dec9")
      session.devId should be ("dev_id1")
    }
  }
}

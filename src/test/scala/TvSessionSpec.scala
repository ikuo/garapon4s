package com.github.ikuo.garapon4s

import java.net.URL
import uk.co.bigbeeconsultants.http.request.RequestBody
import uk.co.bigbeeconsultants.http.header.Headers
import uk.co.bigbeeconsultants.http.HttpClient
import model.SearchResult

class TvSessionSpec extends UnitSpec {
  trait Fixture {
    val ip = "192.168.0.2"
    val gtvsession = "session1"
    val devId = "devid1"
    lazy val httpClient = mock[HttpClient]
    lazy val session =
      new TvSession(ip, gtvsession, devId, HttpClientFactory { httpClient })

    protected def jsonFixure(localPath: String) =
      io.Source.fromFile(s"./src/test/json/${localPath}.json").mkString
  }

  describe("#search") {
    describe("with key") {
      it("should return a SearchResult") {
        new Fixture {
          (httpClient.post(_: URL, _: Option[RequestBody], _: Headers)).
            expects(new URL("http://192.168.0.2/gapi/v3/search?dev_id=devid1&gtvsession=session1"), *, *).
            returning(
              MockResponse.ofJson(jsonFixure("search_result/1")))

          val result = session.search(key = "ニュース")
          result should not be (null)
          result shouldBe a [SearchResult]
        }
      }
    }
  }
}

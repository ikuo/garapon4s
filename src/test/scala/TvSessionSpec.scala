package com.github.ikuo.garapon4s

import java.net.URL
import uk.co.bigbeeconsultants.http.request.RequestBody
import uk.co.bigbeeconsultants.http.header.Headers
import uk.co.bigbeeconsultants.http.HttpClient
import model.SearchResult

class TvSessionSpec extends UnitSpec {
  trait Fixture extends JsonFixture {
    val ip = "192.168.0.2"
    val gtvsession = "session1"
    val devId = "devid1"
    lazy val httpClient = mock[HttpClient]
    lazy val session =
      new TvSession(ip, 80, 1935, gtvsession, devId, HttpClientFactory { httpClient })
  }

  describe("#search") {
    describe("with key") {
      it("should return a SearchResult") {
        new Fixture {
          (httpClient.post(_: URL, _: Option[RequestBody], _: Headers)).
            expects(new URL("http://192.168.0.2:80/gapi/v3/search?dev_id=devid1&gtvsession=session1"), *, *).
            returning(
              MockResponse.ofJson(jsonFixture("search_result/0")))//TODO 1

          val result = session.search(key = "ニュース")
          result should not be (null)
          result shouldBe a [SearchResult]
        }
      }
    }
  }

  describe("#addFavorite") {
    it("should call favorite API") {
      new Fixture {
        (httpClient.post(_: URL, _: Option[RequestBody], _: Headers)).
          expects(new URL("http://192.168.0.2:80/gapi/v3/favorite?dev_id=devid1&gtvsession=session1"), *, *).
          returning(
            MockResponse.ofJson("{\"status\":1,\"version\":\"GTV3.1\"}"))

        session.addFavorite("tvid1", 1)
      }
    }
  }

  describe("#getChannels") {
    it("returns channels") {
      new Fixture {
        (httpClient.get(_: URL, _: Headers)).
          expects(new URL("http://192.168.0.2:80/gapi/v3/channel?dev_id=devid1&gtvsession=session1"), *).
          returning(
            MockResponse.ofJson(jsonFixture("channel_result/1")))
        val channels = session.getChannels
        channels should not be (null)
        channels.size should be (8)
      }
    }
  }
}

package com.github.ikuo.garapon4s

import uk.co.bigbeeconsultants.http.header.{Headers, CookieJar, MediaType}
import uk.co.bigbeeconsultants.http.request.Request
import uk.co.bigbeeconsultants.http.response.{Response, ResponseBody, Status}

import uk.co.bigbeeconsultants.http.response.StringResponseBody

class MockResponse(request: Request, status: Status, body: ResponseBody, headers: Headers, cookies: Option[CookieJar])
extends Response(request, status, body, headers, cookies)

object MockResponse {
  def ofJson(json: String) = {
    val body = new StringResponseBody(
      json,
      new MediaType("application", "json", Some("utf-8"))
    )
    new MockResponse(null, null, body, null, null)
  }
}

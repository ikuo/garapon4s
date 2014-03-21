package com.github.ikuo.garapon4s.model

import com.github.ikuo.garapon4s._

class WebAuthResultSpec extends UnitSpec {
  describe(".parse") {
    it("should return a WebAuthResult") {
      new TextFixture {
        val result = WebAuthResult.parse(textFixture("web_auth/1"))
        result should not be (null)
        result.ipAddress should be ("192.168.1.5")
        result.privateIpAddress should be ("192.168.1.5")
        result.globalIpAddress should be ("192.0.2.1")
        result.portHttp should be (80)
        result.portTs should be (1935)
        result.gtvVersion should be ("GTV3.0")
      }
    }
  }
}

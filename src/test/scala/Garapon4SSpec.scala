package com.github.ikuo.garapon4s

import org.scalatest.FunSpec

class Garapon4SSpec extends FunSpec {
  describe("Adding 1 to 1") {
    it("should equals 2"){
      assert(1+1 == 2)
    }
  }
  //TODO spec for TvClient, TvSession
  /*
    val json = """{"version":"GTV3.1401190","status":1,"login":1,"gtvsession":"a9cfaa236dda032a39a47a77a961dec9"}"""
  */
}


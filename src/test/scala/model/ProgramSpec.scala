package com.github.ikuo.garapon4s.model

import com.github.ikuo.garapon4s._
import com.github.ikuo.garapon4s.model._

class ProgramSpec extends UnitSpec {
  describe("#parsedStartDate") {
    it("should return a parsed date") {
      val p = new Program()
      p.setStartdate("2013-03-11 18:10:00")
      val date = p.parsedStartDate
      date should not be (null)
    }
  }
}

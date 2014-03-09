package com.github.ikuo.garapon4s.model

import com.github.ikuo.garapon4s._

class SearchResultSpec extends UnitSpec {
  trait Fixture extends JsonFixture

  describe(".parse") {
    it("should return a SearchResult with programs") {
      new Fixture {
        val result = SearchResult.parse(jsonFixtureAsInputStream("search_result/0"))
        result should not be (null)
        result shouldBe a [SearchResult]
        result.programs.head.gtvId should be ("1SJP7E871394280000")
      }
    }
  }
}

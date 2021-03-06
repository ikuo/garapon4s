package com.github.ikuo.garapon4s.model

import com.github.ikuo.garapon4s._

class SearchResultSpec extends UnitSpec {
  describe(".parse") {
    describe("without listener") {
      it("should return a SearchResult with programs") {
        new JsonFixture {
          val result = SearchResult.parse(jsonFixtureAsInputStream("search_result/1")).get
          result should not be (null)
          result shouldBe a [SearchResult]
          result.status should be (1)
          result.hit should be (9849)
          result.version should be ("GTV3.1401190")

          val program = result.programs.head
          program.gtvId should be ("1SJP7E871394280000")
          program.ch should be (32391)
          program.description should be ("飛行のススメ")
          program.genre should equal (Array("5/2", "8/7", "9/2"))
        }
      }
    }

    describe("with listener") {
      trait Fixture extends JsonFixture {
        var iterator: Iterator[Program] = null
      }

      describe("when program iterator is consumed") {
        it("should iterates programs without exception") {
          new Fixture {
            val listener = new SearchResultListener {
              override def notifyPrograms(programs: Iterator[Program]) {
                iterator = programs
                iterator.toList
              }
            }

            SearchResult.parse(jsonFixtureAsInputStream("search_result/1"), listener)

            iterator should not be (null)
          }
        }
      }

      describe("when program iterator is not consumed") {
        it("should throw an IllegalStateException") {
          new Fixture {
            intercept[IllegalStateException] {
              val listener = new SearchResultListener {
                override def notifyPrograms(programs: Iterator[Program]) {
                  iterator = programs
                }
              }

              SearchResult.parse(jsonFixtureAsInputStream("search_result/1"), listener)
            }
          }
        }
      }
    }
  }
}

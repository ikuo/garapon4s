package com.github.ikuo.garapon4s

import java.io.ByteArrayInputStream;

trait JsonFixture {
  def jsonFixture(localPath: String) =
    io.Source.fromFile(s"./src/test/json/${localPath}.json").mkString

  def jsonFixtureAsInputStream(localPath: String) =
    new ByteArrayInputStream(jsonFixture(localPath).getBytes)
}

package com.github.ikuo.garapon4s

import java.io.ByteArrayInputStream;

trait TextFixture {
  def textFixture(localPath: String) =
    io.Source.fromFile(s"./src/test/text/${localPath}.text").mkString

  def textFixtureAsInputStream(localPath: String) =
    new ByteArrayInputStream(textFixture(localPath).getBytes)
}

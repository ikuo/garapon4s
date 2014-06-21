package com.github.ikuo.garapon4s.model

trait SearchResultListener {
  def notifyStartParsing = ()
  def notifyVersion(value: String) = ()
  def notifyStatus(value: Int) = ()
  def notifyHit(value: Int) = ()
  def notifyPrograms(programs: Iterator[Program]) = ()
  def getResult: Option[SearchResult] = None
}

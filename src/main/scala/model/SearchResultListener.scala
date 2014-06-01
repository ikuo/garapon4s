package com.github.ikuo.garapon4s.model

trait SearchResultListener {
  def notifyStartParsing = ()
  def notifyStatus(value: Int) = ()
  def notifyHit(value: Int) = ()
  def notifyVersion(value: String) = ()
  def notifyPrograms(programs: Iterator[Program]) = ()
  def getResult: SearchResult = null
}

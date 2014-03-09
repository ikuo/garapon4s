package com.github.ikuo.garapon4s.model

trait SearchResultListener {
  def notifyStatus(value: Int)
  def notifyHit(value: Int)
  def notifyVersion(value: String)
  def notifyPrograms(programs: Stream[Program])
  def getResult: SearchResult = null
}

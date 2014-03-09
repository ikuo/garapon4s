package com.github.ikuo.garapon4s.model

class Program(
  var gtvId: String,
  var startDate: String
) {
  def this() = { this(null, null) }
  def setGtvid(v: String) { this.gtvId = v }
  def setStartdate(v: String) { this.startDate = v }
}

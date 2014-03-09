package com.github.ikuo.garapon4s.model

class Caption(
  var text: String,
  var time: String
) {
  def this() = { this(null, null) }
  def setCaption_text(v: String) { this.text = v }
  def setCaption_time(v: String) { this.time = v }
}

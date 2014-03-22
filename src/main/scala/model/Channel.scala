package com.github.ikuo.garapon4s.model

class Channel(var name: String, var hashTag: String) {
  def this() = this(null, null)
  def setCh_name(v: String) = { this.name = v }
  def setHash_tag(v: String) = { this.hashTag = v }
  override def toString = s"Channel(${name}, ${hashTag})"
}

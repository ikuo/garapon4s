package com.github.ikuo.garapon4s.model

import java.util.HashMap
import collection.JavaConversions._
import com.github.ikuo.garapon4s.{MalformedJsonResponse}

class ChannelResult(
  var status: Int,
  var version: String,
  var channels: Map[Int, Channel]
) {
  def this() = this(-1, null, null)
  def setStatus(v: Int) { this.status = v }
  def setVersion(v: String) { this.version = v }
  def setCh_list(v: HashMap[String, Channel]) {
    val map = collection.mutable.Map[Int, Channel]()
    for (entry <- v.entrySet) {
      map(entry.getKey.toInt) = entry.getValue
    }
    this.channels = map.toMap
  }
}

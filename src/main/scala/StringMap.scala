package com.github.ikuo.garapon4s

import collection.mutable.HashMap

/**
 * An utility to make request body.
 */
class StringMap extends HashMap[String, String] {
  val NA = -1

  override def update(key: String, value: String) {
    if (value != null) { super.update(key, value) }
  }

  def update(key: String, value: Int) {
    if (value != NA) { super.update(key, value.toString) }
  }

  def update(key: String, value: Option[String]) {
    value.map(super.update(key, _))
  }
}

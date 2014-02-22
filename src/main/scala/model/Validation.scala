package com.github.ikuo.garapon4s.model

trait Validation {
  protected def nonEmpty(name: String, value: String) {
    if ((value == null) || (value == "")) {
      sys.error(s"${name} is empty")
    }
  }

  protected def nonNegative(name: String, value: Int) {
    if (value < 0) sys.error(s"${name} is negative: ${value}")
  }
}

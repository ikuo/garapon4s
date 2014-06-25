package com.github.ikuo.garapon4s

trait IpCheck {
  protected def isPrivate(ip: String): Boolean =
    ip.split('.') match {
      case Array("192", "168", _, _) => true  // class C
      case Array("10", _, _, _) => true       // class A
      case Array("172", a, _, _) => {         // class B
        val i = a.toInt
        (i >= 16 && i <= 31)
      }
      case _ => false
    }
}

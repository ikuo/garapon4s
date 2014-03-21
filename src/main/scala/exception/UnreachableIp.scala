package com.github.ikuo.garapon4s

class UnreachableIp(val ips: List[String])
extends RuntimeException(ips.mkString(","))

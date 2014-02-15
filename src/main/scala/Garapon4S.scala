package com.github.ikuo.garapon4s

object Garapon4S {
  def main(args: Array[String]) {
    val src = new java.io.FileInputStream("./garapon4s.properties")
    try {
      val config = new java.util.Properties()
      config.load(src)
      val client = new TvClient(config.get("dev_id").toString)
      client.newSessionViaLAN(
        java.net.InetAddress.getByName(config.get("default.ip").toString),
        config.get("default.user").toString,
        config.get("default.md5password").toString
      )
    } finally { src.close }
  }
}


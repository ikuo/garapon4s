package com.github.ikuo.garapon4s

object Garapon4S {
  def main(args: Array[String]) {
    val client = new TvClient(config.get("dev_id").toString)
    val session =
      if (ip == null) {
        client.newSession(
          config.get("default.user").toString,
          config.get("default.md5password").toString
        )
      } else {
        client.newSessionByIp(ip.toString,
          config.get("default.user").toString,
          config.get("default.md5password").toString
        )
      }

    println("gtvsession=" + session.gtvsession)

    val results = session.search(key = "ニュース")
    println("hits=" + results.hit)
  }

  private lazy val ip = config.get("default.ip")

  private lazy val config: java.util.Properties = {
    val props = new java.util.Properties()
    val src = new java.io.FileInputStream("./garapon4s.properties")
    try { props.load(src) } finally { src.close }
    props
  }
}

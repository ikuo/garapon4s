addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

// ls.implicit.ly {{{
resolvers ++= Seq(
  Classpaths.sbtPluginReleases,
  Opts.resolver.sonatypeReleases
)

addSbtPlugin("me.lessis" % "ls-sbt" % "0.1.3")
// }}}

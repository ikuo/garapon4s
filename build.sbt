name := "Garapon4S"

organization := "com.github.ikuo"

version := "0.0.1"

scalaVersion := "2.10.3"

initialCommands := "import com.github.ikuo.garapon4s._"

seq(lsSettings :_*)

// Bee Client {{{
libraryDependencies ++= Seq(
    "uk.co.bigbeeconsultants" %% "bee-client" % "0.21.+",
    "org.slf4j" % "slf4j-api" % "1.7.+"
)

resolvers += "Big Bee Consultants" at "http://repo.bigbeeconsultants.co.uk/repo"
// }}}

// ScalaTest
resolvers ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test" withSources() withJavadoc(),
  "org.scalacheck" % "scalacheck_2.10" % "1.11.3" % "test" withSources() withJavadoc()
)

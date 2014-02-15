name := "Garapon4S"

organization := "com.github.ikuo"

version := "0.0.1"

scalaVersion := "2.10.3"

initialCommands := "import com.github.ikuo.garapon4s._"

seq(lsSettings :_*)

// ScalaTest
resolvers ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test" withSources() withJavadoc(),
  "org.scalacheck" % "scalacheck_2.10" % "1.11.3" % "test" withSources() withJavadoc()
)

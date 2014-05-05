name := "Garapon4S"

organization := "com.github.ikuo"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.3"

initialCommands := "import com.github.ikuo.garapon4s._"

seq(lsSettings :_*)

libraryDependencies ++= Seq(
    "uk.co.bigbeeconsultants" %% "bee-client" % "0.21.+",
    "org.slf4j" % "slf4j-api" % "1.7.+",
    "com.fasterxml.jackson.core" % "jackson-core" % "2.3.0",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.3.0"
)

resolvers ++= Seq(
  "Sonatype Releases" at "http://oss.sonatype.org/content/repositories/releases",
  "Big Bee Consultants" at "http://repo.bigbeeconsultants.co.uk/repo"
)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test" withSources() withJavadoc(),
  "org.scalacheck" % "scalacheck_2.10" % "1.11.3" % "test" withSources() withJavadoc(),
  "org.scalamock" %% "scalamock-scalatest-support" % "3.1.RC1" % "test"
)

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomExtra := (
  <url>https://github.com/ikuo/garapon4s</url>
  <licenses>
    <license>
      <name>The Apache Software License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:ikuo/garapon4s.git</url>
    <connection>scm:git:git@github.com:ikuo/garapon4s.git</connection>
  </scm>
  <developers>
    <developer>
      <id>ikuo</id>
      <name>Ikuo Matsumura</name>
      <url>https://github.com/ikuo/</url>
    </developer>
  </developers>)

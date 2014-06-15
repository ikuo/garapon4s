# Garapon4S [![Build Status](https://travis-ci.org/ikuo/garapon4s.svg?branch=master)](https://travis-ci.org/ikuo/garapon4s)
A client library of Garapon TV API for scala.
Designed for Android App (but independent of Android SDK).
Garapon TV API Version 3 is supported.

## Usage

Setup dependencies in sbt:

```sbt
resolvers ++= Seq(
  "Sonatype Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  "Big Bee Consultants" at "http://repo.bigbeeconsultants.co.uk/repo"
)

libraryDependencies += "com.github.ikuo" % "garapon4s_2.10" % "0.2.1-SNAPSHOT"
```

Code example:

```scala
import com.github.ikuo.garapon4s._

val client = new TVClient("my_developer_id")
val session = client.newSession("my_user_name", "my_md5password")

// 1) Print session ID
println("sessionID = " + session.gtvsession)

// 2) Search programs by keyword
val results = session.search(key = "News")
println("search hit = " + results.hit)

// 3) Get web viewer URL for the first program in the search result
val p1 = results.programs(0).gtvId
  // => http://192.168.1.xx:80/main.garapon#/viewer/player.garapon?gtvid=1SJP7FE61399078800
```

See also `main()` method in `Garapon4S.scala`

## Using in Android App

This section describes a) manual setup of proguard and android manifest,
and b) generating a project by a giter8 template.

See also [Garaponoid](https://github.com/ikuo/garaponoid) that use Garapon4S in Android.

### a) Set up manually
Keep Jackson related classes and methods in proguard settings:
```
-keep class com.fasterxml.jackson.databind.** { *; }
-keepattributes *Annotation*,EnclosingMethod
-keep public class com.github.ikuo.garapon4s.model.** {
  public void set*(***);
  public *** get*();
}
```

Add the following `uses-permission`s in AndroidManifest.xml:

```xml
<manifest ...>
  ...
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
  ...
</manifest>
```

### b) Set up using project template
The following descriptions are tested under [sbt android-plugin](https://github.com/jberkel/android-plugin) based environment.

Install [giter8](https://github.com/n8han/giter8) and generate a project using the template of
[garapo4s_client](https://github.com/ikuo/android-app.g8/tree/garapon4s_client)
as follows:
```
$ g8 ikuo/android-app -b garapon4s_client
...(snip)...
Template applied in ./garaponapp1
```

And replace place holder of user name, MD5 password, and developer ID as follows:
```
$ cd ./garaponapp1
$ vi src/main/scala/MainActivity.scala
# Edit my_user_name, my_md5password, my_developer_id in the source file.
```

Then build and start the app in sbt:
```
$ sbt
> package
> start
```

## Development
### Running Specs
```
$ sbt
> test
```

### Running Specs with HTTP Requests
Copy garapon4s.properties.sample to garapon4s.properties, and edit entries to match your environments.

```
$ sbt
> run
gtvsession=e8dafbf3763d83f23364xxxxxxxxxxxx
```

It will show a gtvsession ID received from your Garapon TV device.

See also `src/main/scala/Garapon4S.scala`.

### Generating Scaladoc
```
$ sbt
> doc
[info] Generating Scala API documentation for main sources to .../target/scala-2.10/api...
```

## License
Apache License 2.0

## Credits
Garapon4S is using the following works:
- [Jackson JSON Processor](http://wiki.fasterxml.com/JacksonHome) - Apache License 2.0
- [Bee Client](http://www.bigbeeconsultants.co.uk/bee-client) - MIT License
- [SLF4J](http://www.slf4j.org/) - MIT License

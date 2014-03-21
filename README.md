# Garapon4S
A client library of GaraponTV API for scala. (work in progress)

# Usage example
See `main()` method in `Garapon4S.scala`

## 1. Run test
Install scalamock 3.1-SNAPSHOT into your local repository as follows:

```
$ git clone git@github.com:paulbutcher/ScalaMock.git
$ cd ScalaMock
$ sbt
ScalaMock:master:3.1-SNAPSHOT> publish-local
```

Then run `test` task in sbt console:

```
$ sbt
> test
```

## 2. Run a example of real HTTP
Copy garapon4s.properties.sample to garapon4s.properties, and edit entries to match your environments.

```
$ sbt
> run
application/json;charset=utf-8
gtvsession=e8dafbf3763d83f23364xxxxxxxxxxxx
```

It will show a gtvsession ID received from your Garapon TV device.

See also `src/main/scala/Garapon4S.scala`.

## TODO
- Support Web Auth API
- Support Favorite, Channel, Play, and Thumbs API
- Handle errors in response

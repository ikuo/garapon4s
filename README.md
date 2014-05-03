# Garapon4S [![Build Status](https://travis-ci.org/ikuo/garapon4s.svg?branch=master)](https://travis-ci.org/ikuo/garapon4s)
A client library of GaraponTV API for scala.
Designed for Android App.

# Usage example
See `main()` method in `Garapon4S.scala`

## 1. Test
```
$ sbt
> test
```

## 2. Run a example of real HTTP
Copy garapon4s.properties.sample to garapon4s.properties, and edit entries to match your environments.

```
$ sbt
> run
gtvsession=e8dafbf3763d83f23364xxxxxxxxxxxx
```

It will show a gtvsession ID received from your Garapon TV device.

See also `src/main/scala/Garapon4S.scala`.

## TODO
- API Documentation

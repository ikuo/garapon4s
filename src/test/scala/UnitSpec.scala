package com.github.ikuo.garapon4s

import org.scalatest._
import org.scalamock.scalatest.MockFactory
import org.scalamock.scalatest.proxy.{MockFactory => ProxyMockFactory}
//import org.scalatest.mock._

abstract class UnitSpec extends FunSpec
  with Matchers
  with MockFactory
  //with ProxyMockFactory
  //with MockitoSugar

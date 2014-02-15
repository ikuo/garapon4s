package com.github.ikuo.garapon4s

import uk.co.bigbeeconsultants.http.HttpClient

trait HttpClientFactory {
  def create: HttpClient
}

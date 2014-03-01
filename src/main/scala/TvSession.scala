package com.github.ikuo.garapon4s

import java.util.Date
import java.net.URL
import uk.co.bigbeeconsultants.http.HttpClient
import uk.co.bigbeeconsultants.http.request.RequestBody
import TvSession._

//** TV session */
class TvSession(
  val ip: String,
  val gtvsession: String,
  val devId: String,
  val httpClientFactory: HttpClientFactory = HttpClientFactory({ new HttpClient })
) {
  /**
   * Returns search result from the TV device.
   *
   * @param n the number of results per page
   * @param p the page
   * @param s the search target
   * @param key the search string
   * @param gtvid the program ID
   * @param gtvidlist a list of the program IDs
   * @param genre0 the major genre
   * @param genre1 the minor genre
   * @param ch the channel number
   * @param dt the mode wheter sdate and edate should be interpreted as start time or end time
   * @param sdate the start date time
   * @param edate the end date time
   * @param rank Some("all") when searching favorites
   * @param sort the sorting order
   */
  def search(
    n: Option[Int] = None,
    p: Option[Int] = None,
    s: Option[SearchTarget] = None,
    key: Option[String] = None,
    gtvid: Option[String] = None,
    gtvidlist: Option[String] = None,
    genre0: Option[Int] = None,
    genre1: Option[Int] = None,
    ch: Option[Int] = None,
    dt: Option[DateMode] = None,
    sdate: Option[Date] = None,
    edate: Option[Date] = None,
    rank: Option[String] = None,
    sort: Option[SortOrder] = None,
    video: Option[String] = None
  ) = {
    val url = s"http://${ip}/gapi/v3/search?dev_id=${devId}&gtvsession=${gtvsession}"
    val body = Some(RequestBody(Map("key" -> key.get)))
    val response =
      httpClientFactory.create.post(
        new URL(url),
        body
      )
    println(response.body)
    null
  }
}

object TvSession {
  /** Enumeration of search target parameter */
  sealed abstract class SearchTarget(val code: Char)
  case object EPG extends SearchTarget('e')
  case object Caption extends SearchTarget('c')

  /** Enumeration of date mode parameter */
  sealed abstract class DateMode(val code: Char)
  case object AsStart extends DateMode('s')
  case object AsEnd extends DateMode('e')

  /** Enumeration of sort order */
  sealed abstract class SortOrder(val code: String)
  case object Desc extends SortOrder("std")
  case object Asc extends SortOrder("sta")
}

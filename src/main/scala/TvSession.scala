package com.github.ikuo.garapon4s

import java.util.Date
import java.net.URL
import uk.co.bigbeeconsultants.http.HttpClient
import uk.co.bigbeeconsultants.http.request.RequestBody
import uk.co.bigbeeconsultants.http.header.MediaType
import model.SearchResult
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
    n: Int = NA,
    p: Int = NA,
    s: SearchTarget = null,
    key: String = null,
    gtvid: String = null,
    gtvidlist: String = null,
    genre0: Int = NA,
    genre1: Int = NA,
    ch: Int = NA,
    dt: DateMode = null,
    sdate: Date = null,
    edate: Date = null,
    rank: String = null,
    sort: SortOrder = null,
    video: String = null
  ): SearchResult = {
    val url = s"http://${ip}/gapi/v3/search?dev_id=${devId}&gtvsession=${gtvsession}"
    val body =
      Some(RequestBody(Map("key" -> key), //TODO handle null key
        MediaType.APPLICATION_OCTET_STREAM))
    val response =
      httpClientFactory.create.post(
        new URL(url),
        body, Nil
      )

    SearchResult(response.body.inputStream)
  }
}

object TvSession {
  val NA = -1

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

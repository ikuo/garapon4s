package com.github.ikuo.garapon4s

import java.util.Date
import java.net.URL
import java.text.SimpleDateFormat
import uk.co.bigbeeconsultants.http.HttpClient
import uk.co.bigbeeconsultants.http.request.RequestBody
import uk.co.bigbeeconsultants.http.header.MediaType
import com.fasterxml.jackson.databind.ObjectMapper
import model._
import TvSession._

//** TV session */
class TvSession(
  val ip: String,
  val portHttp: Int,
  val portTs: Int,
  val gtvsession: String,
  val devId: String,
  val httpClientFactory: HttpClientFactory = HttpClientFactory({ new HttpClient })
) {
  private lazy val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  private lazy val objectMapper = new ObjectMapper
  val baseUrl = s"http://${ip}:${portHttp}/gapi/v3/"
  val queryPrefix = s"?dev_id=${devId}&gtvsession=${gtvsession}"

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
   * @param resultListener observer to get sub-results as soon as possible in JSON parsing
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
    video: String = null,
    resultListener: SearchResultListener = null
  ): SearchResult = {
    val url = s"${baseUrl}search${queryPrefix}"

    val map = new StringMap()
    map("n") = n
    map("p") = p
    map("s") = Option(s).map(_.code.toString)
    map("key") = key
    map("gtvid") = gtvid
    map("gtvidList") = gtvidlist
    map("genre0") = genre0
    map("genre1") = genre1
    map("ch") = ch
    map("dt") = Option(dt).map(_.code.toString)
    map("sdate") = Option(sdate).map(formatDate(_))
    map("edate") = Option(edate).map(formatDate(_))
    map("rank") = rank
    map("sort") = Option(sort).map(_.code)
    map("video") = video

    val body = Some(RequestBody(map.toMap, MediaType.APPLICATION_OCTET_STREAM))
    val response = httpClientFactory.create.post(new URL(url), body, Nil)

    SearchResult.parse(response.body.inputStream, resultListener)
  }

  def logout {
    val url = s"${baseUrl}auth${queryPrefix}"
    val body = Some(RequestBody(Map("type" -> "logout")))
    val response = httpClientFactory.create.post(new URL(url), body, Nil)
    objectMapper.readValue(response.body.inputStream, classOf[AuthResult]).
      validate(loginResult = false)
  }

  def addFavorite(gtvid: String, rank: Int) {
    val url = s"${baseUrl}favorite${queryPrefix}"
    val body = Some(RequestBody(Map("gtvid" -> gtvid, "rank" -> rank.toString)))
    val response = httpClientFactory.create.post(new URL(url), body, Nil)
    objectMapper.readValue(response.body.inputStream, classOf[FavoriteResult]).
      validate
  }

  def removeFavorite(gtvid: String) = addFavorite(gtvid, 0)

  def getChannels = {
    val url = s"${baseUrl}channel${queryPrefix}"
    val response = httpClientFactory.create.get(new URL(url), Nil)
    objectMapper.readValue(response.body.inputStream, classOf[ChannelResult]).
      channels
  }

  private def formatDate(date: Date) =
    dateFormat.synchronized { dateFormat.format(date) }
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

package com.github.ikuo.garapon4s.model

import Program._
import java.text.SimpleDateFormat
import java.util.Date

class Program(
  var gtvId: String,
  var startDate: String,
  var duration: String,
  var ch: Int,
  var title: String,
  var description: String,
  var genre: Array[String],
  var favorite: Int,
  var bc: String,
  var bcTags: String,
  var ts: Int,
  var captionHit: Int,
  var caption: Array[Caption],
  var audioCh: String
) {
  def this() = { this(null, null, null, NA, null, null, null, NA, null, null, NA, NA, null, null) }
  def setGtvid(v: String) { this.gtvId = v }
  def setStartdate(v: String) { this.startDate = v }
  def setDuration(v: String) { this.duration = v }
  def setCh(v: Int) { this.ch = v }
  def setTitle(v: String) { this.title = v }
  def setDescription(v: String) { this.description = v }
  def setGenre(v: Array[String]) { this.genre = v }
  def setFavorite(v: Int) { this.favorite = v }
  def setBc(v: String) { this.bc = v }
  def setBc_tags(v: String) { this.bcTags = v }
  def setTs(v: Int) { this.ts = v }
  def setCaption_hit(v: Int) { this.captionHit = v }
  def setCaption(v: Array[Caption]) { this.caption = v }
  def setAudio_ch(v: String) { this.audioCh = v }

  def parsedStartDate: Date = dateFormat.parse(startDate)
}

object Program {
  val NA = -1
  lazy val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
}

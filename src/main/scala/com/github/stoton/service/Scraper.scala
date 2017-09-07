package com.github.stoton.service

import com.github.stoton.domain.TimetableIndexItem

import scala.collection.mutable.ListBuffer

trait Scraper {
  def parseDataFromZsatTimetable(): ListBuffer[TimetableIndexItem]
}

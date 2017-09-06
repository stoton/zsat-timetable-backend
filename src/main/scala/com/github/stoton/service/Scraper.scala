package com.github.stoton.service

import com.github.stoton.domain.TimeTableIndexItem

import scala.collection.mutable.ListBuffer

trait Scraper {
  def scrapDateFromPage(url: String): ListBuffer[TimeTableIndexItem]
}

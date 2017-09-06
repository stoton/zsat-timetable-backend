package com.github.stoton.controller

import java.util

import com.github.stoton.domain.TimeTableIndexItem
import com.github.stoton.service.Scraper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RestController}

import collection.JavaConverters._

@RestController
class TimeTableIndexItemController @Autowired()(private val scraper: Scraper) {
  val INDEX_URL: String = "http://szkola.zsat.linuxpl.eu/planlekcji/lista.html"

  @GetMapping(Array("/timetable"))
  def timeTableIndexItem(): util.List[TimeTableIndexItem] = {
    val list = scraper.scrapDateFromPage(INDEX_URL)
    list.asJava
  }
}

package com.github.stoton.controller

import java.util

import com.github.stoton.domain.TimetableIndexItem
import com.github.stoton.service.Scraper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.{GetMapping, RestController}

import collection.JavaConverters._

@RestController
class TimetableIndexItemController @Autowired()(private val scraper: Scraper) {

  @GetMapping(Array("/timetable"))
  def timeTableIndexItem(): util.List[TimetableIndexItem] = {
    val list = scraper.parseDataFromZsatTimetable()
    list.asJava
  }
}
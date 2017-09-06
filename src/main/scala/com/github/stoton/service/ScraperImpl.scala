package com.github.stoton.service

import java.util

import com.github.stoton.domain.TimeTableIndexItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Service

import scala.collection.mutable.ListBuffer

@Service
class ScraperImpl() extends Scraper {
  val ALL_ELEMENTS_FROM_H4 = "h4 + ul"
  val CLASSES_ELEMENTS_FROM_PAGE = "ul > li > a[href^=plany/o]"
  val TEACHERS_ELEMENTS_FROM_PAGE = "ul > li > a[href^=plany/n]"
  val CLASSROOMS_ELEMENTS_FROM_PAGE = "ul > li > a[href^=plany/s"
  val STUDENT = "STUDENT"
  val TEACHER = "TEACHER"
  val CLASSROOM = "CLASSROOM"

  override def scrapDateFromPage(url: String): ListBuffer[TimeTableIndexItem] = {

    val document = Jsoup.connect(url).get()

    def scrapIndexItem() =  {
      val list = new ListBuffer[TimeTableIndexItem]

      val elements = document.select(ALL_ELEMENTS_FROM_H4)

      val classes = elements.select(CLASSES_ELEMENTS_FROM_PAGE)
      val teachers = elements.select(TEACHERS_ELEMENTS_FROM_PAGE)
      val classrooms = elements.select(CLASSROOMS_ELEMENTS_FROM_PAGE)

      val classesIterator = classes.listIterator
      val teachersIterator = teachers.listIterator
      val classroomsIterator = classrooms.listIterator

      extractAndAddToList(list)(classesIterator)(STUDENT)
      extractAndAddToList(list)(teachersIterator)(TEACHER)
      extractAndAddToList(list)(classroomsIterator)(CLASSROOM)

      list
    }

    val result = scrapIndexItem()
    result
  }

  private def extractAndAddToList(list: ListBuffer[TimeTableIndexItem])
                                 (iterator: util.ListIterator[Element])
                                 (category: String): Unit = {
    while(iterator.hasNext) {
      var timeTableIndexItem = TimeTableIndexItem(iterator.next.text, category)
      list += timeTableIndexItem
    }
  }
}

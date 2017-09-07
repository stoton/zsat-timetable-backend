package com.github.stoton.service

import java.util

import com.github.stoton.domain.TimetableIndexItem
import com.github.stoton.tools.{CssQuery, TimetableType}
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.springframework.stereotype.Service

import scala.collection.mutable.ListBuffer

@Service
class ScraperImpl() extends Scraper {
  val INDEX_URL: String = "http://szkola.zsat.linuxpl.eu/planlekcji/lista.html"

  override def parseDataFromZsatTimetable(): ListBuffer[TimetableIndexItem] = {
    val document = Jsoup.connect(INDEX_URL).get()

    def scrapIndexItem() =  {
      val list = new ListBuffer[TimetableIndexItem]

      val elements = document.select(CssQuery.AllElementsFromH4.toString)

      val classes = elements.select(CssQuery.ClassesElementsFromPage.toString)
      val teachers = elements.select(CssQuery.TeachersElementsFromPage.toString)
      val classrooms = elements.select(CssQuery.ClassroomsElementsFromPage.toString)

      val classesIterator = classes.listIterator
      val teachersIterator = teachers.listIterator
      val classroomsIterator = classrooms.listIterator

      extractAndAddToList(list)(classesIterator)(TimetableType.Student.toString)
      extractAndAddToList(list)(teachersIterator)(TimetableType.Teacher.toString)
      extractAndAddToList(list)(classroomsIterator)(TimetableType.Classroom.toString)

      list
    }

    val result = scrapIndexItem()
    result
  }

  private def extractAndAddToList(list: ListBuffer[TimetableIndexItem])
                                 (iterator: util.ListIterator[Element])
                                 (category: String): Unit = {
    while(iterator.hasNext) {
      val timeTableIndexItem = TimetableIndexItem(iterator.next.text, category)
      list += timeTableIndexItem
    }
  }
}

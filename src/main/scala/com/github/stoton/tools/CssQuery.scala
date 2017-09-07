package com.github.stoton.tools

object CssQuery extends Enumeration {
  type CssQuery = Value
  val AllElementsFromH4 = Value("h4 + ul")
  val ClassesElementsFromPage = Value("ul > li > a[href^=plany/o]")
  val TeachersElementsFromPage = Value("ul > li > a[href^=plany/n]")
  val ClassroomsElementsFromPage = Value("ul > li > a[href^=plany/s]")


}

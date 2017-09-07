package com.github.stoton.domain


import com.fasterxml.jackson.annotation.{JsonCreator}

import scala.beans.BeanProperty

case class TimetableIndexItem @JsonCreator()(@BeanProperty name: String, @BeanProperty `type`: String) {

  override def toString: String = {
    s"$name $$`type`"
  }

}
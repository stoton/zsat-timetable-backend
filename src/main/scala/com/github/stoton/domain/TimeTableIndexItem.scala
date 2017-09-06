package com.github.stoton.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize

import scala.beans.BeanProperty

@JsonSerialize
case class TimeTableIndexItem(@JsonProperty("name") @BeanProperty var name: String,
                              @JsonProperty("type") @BeanProperty var category: String) extends Serializable {
  override def toString: String = {
    s"$name $category"
  }
}

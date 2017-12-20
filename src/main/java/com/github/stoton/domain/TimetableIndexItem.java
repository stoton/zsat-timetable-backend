package com.github.stoton.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class TimetableIndexItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String name;

    private String link;
    private String type;

    @JsonIgnore
    private String url;

    @JsonIgnore
    private String teacherID;

    public TimetableIndexItem(String name, String type, String url, String link) {
        this.name = name;
        this.type = type;
        this.url = url;
        this.link = link;
    }
}

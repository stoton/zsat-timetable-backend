# zsat-timetable-backend

RESTful service for: <a href="https://play.google.com/store/apps/details?id=shemhazai.github.com.timetable&hl=pl">mobile app</a>

The main idea of the service is to provide data from <a href="http://szkola.zsat.linuxpl.eu/planlekcji/index.html"> ZSA-T's timetable </a> whenever client needs that.

Once a day it parses the whole timetable and puts it into a database. And that's why client is easily able to get the data and construct whathever he wants.

Project based on Spring boot in version 1.5.6, Hibernate 5 and Lombok. Jsoup was used for parsing of the html documents. Tests were written in Junit and Mockito.

There is available api - just click this link: http://timetable.shemhazai.com:8080/swagger-ui.html



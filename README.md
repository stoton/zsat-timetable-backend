# zsat-timetable-backend

RESTful service for: <a href="https://play.google.com/store/apps/details?id=shemhazai.github.com.timetable&hl=pl">mobile app</a>

its mainly task is providing data from <a href="http://szkola.zsat.linuxpl.eu/planlekcji/index.html"> ZSA-T's timetable </a> when client needs.

Once per day it's parsing whole timetable and collect into database. And that's why client is easily able to get and construct his no matter for server things.

Project based on Spring boot in version 1.5.6 and Hibernate 5, also using lombok and Jsoup for html documents parsing. Tests were written in Junit and Mockito.

There is available api - just click this link: http://timetable.shemhazai.com:8080/swagger-ui.html


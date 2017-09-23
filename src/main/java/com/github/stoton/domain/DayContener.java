package com.github.stoton.domain;

import java.util.ArrayList;
import java.util.List;

public class DayContener {

    private  List<Lesson> monday;
    private  List<Lesson> tuesday;
    private  List<Lesson> wednesday;
    private  List<Lesson> thursday;
    private  List<Lesson> friday;

    public DayContener() {
    }

    private DayContener(DayContenerBuilder builder) {
        this.monday = builder.monday;
        this.tuesday = builder.tuesday;
        this.wednesday = builder.wednesday;
        this.thursday = builder.thursday;
        this.friday = builder.friday;
    }

    public List<Lesson> getMonday() {
        return monday;
    }

    public List<Lesson> getTuesday() {
        return tuesday;
    }

    public List<Lesson> getWednesday() {
        return wednesday;
    }

    public List<Lesson> getThursday() {
        return thursday;
    }

    public void setMonday(List<Lesson> monday) {
        this.monday = monday;
    }

    public void setTuesday(List<Lesson> tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(List<Lesson> wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(List<Lesson> thursday) {
        this.thursday = thursday;
    }

    public void setFriday(List<Lesson> friday) {
        this.friday = friday;
    }

    public List<Lesson> getFriday() {
        return friday;
    }

    public static class DayContenerBuilder {
        private List<Lesson> monday;
        private List<Lesson> tuesday;
        private List<Lesson> wednesday;
        private List<Lesson> thursday;
        private List<Lesson> friday;

        public DayContenerBuilder monday(List<Lesson> monday) {
            this.monday = monday;
            return this;
        }

        public DayContenerBuilder tuesday(List<Lesson> tuesday) {
            this.tuesday = tuesday;
            return this;
        }

        public DayContenerBuilder wednesday(List<Lesson> wednesday) {
            this.wednesday = wednesday;
            return this;
        }

        public DayContenerBuilder thursday(List<Lesson> thursday) {
            this.thursday = thursday;
            return this;
        }

        public DayContenerBuilder friday(List<Lesson> friday) {
            this.friday = friday;
            return this;
        }

        public DayContener build() {
            return new DayContener(this);
        }

    }



}

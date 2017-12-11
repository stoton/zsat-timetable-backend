package com.github.stoton.domain;

public enum CssQuery {
    ALL_ELEMENTS_FROM_H4("h4 + ul"),
    CLASSES_ELEMENTS_FROM_PAGE("ul > li > a[href^=plany/o]"),
    TEACHERS_ELEMENTS_FROM_PAGE("ul > li > a[href^=plany/n]"),
    CLASSROOMS_ELEMENTS_FROM_PAGE("ul > li > a[href^=plany/s]"),
    URLS("ul > li > a"),
    HTML_TABLE_CLASS(".tabela"),
    TR_ELEMENT("tr"),
    L_CLASS(".l"),
    P_CLASS(".p"),
    O_CLASS(".o"),
    N_CLASS(".n"),
    S_CLASS(".s"),
    NR_CLASS(".nr"),
    HOUR_CLASS(".g"),
    LINK_START_WITH_S("a[href^=s]");

    private final String text;

    CssQuery(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

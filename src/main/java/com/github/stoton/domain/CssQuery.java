package com.github.stoton.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CssQuery {

    ALL_ELEMENTS_FROM_H4_WITH_UL_DESCENDANT("h4 + ul"),
    CLASSES_ELEMENTS_FROM_PAGE("ul > li > a[href^=plany/o]"),
    TEACHERS_ELEMENTS_FROM_PAGE("ul > li > a[href^=plany/n]"),
    CLASSROOMS_ELEMENTS_FROM_PAGE("ul > li > a[href^=plany/s]"),
    LINKS_IN_A_LIST("ul > li > a"),
    HTML_TABLE_CLASS(".tabela"),
    TR_ELEMENT("tr"),
    L_CLASS(".l"),
    P_CLASS(".p"),
    O_CLASS(".o"),
    N_CLASS(".n"),
    S_CLASS(".s"),
    NR_CLASS(".nr"),
    HOUR_CLASS(".g");

    private final String text;

    @Override
    public String toString() {
        return text;
    }
}

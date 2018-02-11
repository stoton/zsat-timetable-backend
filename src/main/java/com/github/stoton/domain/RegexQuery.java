package com.github.stoton.domain;

public enum RegexQuery {

    EXTRACT_STRING_BY_CLASS("[a-zA-Z-żŻ0-9/2.]+"),
    ADD_LEADING_ZERO("(\\b\\d\\b)");

    private final String text;

    RegexQuery(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}

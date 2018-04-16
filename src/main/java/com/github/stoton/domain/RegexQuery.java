package com.github.stoton.domain;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RegexQuery {

    EXTRACT_STRING_BY_CLASS("[a-zA-Z-żŻ0-9/2.]+"),
    ADD_LEADING_ZERO("(\\b\\d\\b)");

    private final String text;

    @Override
    public String toString() {
        return text;
    }
}

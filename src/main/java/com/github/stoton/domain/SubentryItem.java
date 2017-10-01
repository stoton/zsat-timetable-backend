package com.github.stoton.domain;

public abstract class SubentryItem {
    private String text;
    private  boolean clickable;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }
}

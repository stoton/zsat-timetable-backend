package com.github.stoton.domain;

public class Subentry {
    private String primaryText;
    private String secondaryText;
    private String addon;

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    public String getAddon() {
        return addon;
    }

    public void setAddon(String addon) {
        this.addon = addon;
    }

    @Override
    public String toString() {
        return "Subentry{" +
                "primaryText='" + primaryText + '\'' +
                ", secondaryText='" + secondaryText + '\'' +
                ", addon='" + addon + '\'' +
                '}';
    }
}


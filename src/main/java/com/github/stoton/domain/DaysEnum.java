package com.github.stoton.domain;

import java.util.Optional;

public enum DaysEnum {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY;

    public Optional<DaysEnum> next() {
        switch (this) {
            case MONDAY:
                return Optional.of(TUESDAY);
            case TUESDAY:
                return Optional.of(WEDNESDAY);
            case WEDNESDAY:
                return Optional.of(THURSDAY);
            case THURSDAY:
                return Optional.of(FRIDAY);
            case FRIDAY:
                return Optional.of(MONDAY);

            default:
                return Optional.empty();
        }
    }
}

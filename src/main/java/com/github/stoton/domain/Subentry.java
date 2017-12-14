package com.github.stoton.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subentry {

    private String primaryText;
    private String secondaryText;
    private String addon;
}


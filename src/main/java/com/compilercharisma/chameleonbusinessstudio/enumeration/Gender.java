package com.compilercharisma.chameleonbusinessstudio.enumeration;

import lombok.Getter;

public enum Gender {

    MALE("Male"),
    FEMALE("Female"),
    NONBINARY("Non-binary"),
    TRANSGENDER("Transgender"),
    PREFERNOTTORESPOND("Prefer not to respond");

    @Getter
    private final String label;

    Gender(String label) {
        this.label = label;
    }
}

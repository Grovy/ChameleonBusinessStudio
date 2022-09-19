package com.compilercharisma.chameleonbusinessstudio.enumeration;

import lombok.Getter;

public enum UserRole {

    ADMIN("Admin"),
    ORGANIZER("Organizer"),
    PARTICIPANT("Participant"),
    TALENT("Talent");

    @Getter
    private String label;

    UserRole(String label){
        this.label = label;
    }

}

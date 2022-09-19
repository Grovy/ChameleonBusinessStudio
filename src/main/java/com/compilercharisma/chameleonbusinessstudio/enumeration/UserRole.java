package com.compilercharisma.chameleonbusinessstudio.enumeration;

import lombok.Getter;

public enum UserRole {

    ADMIN("ADMIN"),
    ORGANIZER("ORGANIZER"),
    PARTICIPANT("PARTICIPANT"),
    TALENT("TALENT");

    @Getter
    private final String roleName;

    UserRole(String roleName){
        this.roleName = roleName;
    }
}

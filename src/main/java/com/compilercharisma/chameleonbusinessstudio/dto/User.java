package com.compilercharisma.chameleonbusinessstudio.dto;

import com.compilercharisma.chameleonbusinessstudio.enumeration.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {

    private String _id;

    private String firstName;

    private String lastName;

    private String email;

    private UserRole role;

}

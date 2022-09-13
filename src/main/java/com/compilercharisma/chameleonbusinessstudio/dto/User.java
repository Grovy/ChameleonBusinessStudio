package com.compilercharisma.chameleonbusinessstudio.dto;

import com.compilercharisma.chameleonbusinessstudio.enumeration.Gender;
import com.compilercharisma.chameleonbusinessstudio.enumeration.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {

    private String _id;

    private String firstName;

    private String lastName;

    private String email;

    private List<UserRole> roles;

    private Gender gender;

}

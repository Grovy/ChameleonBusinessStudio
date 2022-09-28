package com.compilercharisma.chameleonbusinessstudio.dto;

import com.compilercharisma.chameleonbusinessstudio.enumeration.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {

    /**
     * Vendia Id of the user
     */
    private String _id;

    /**
     * The display name of the user
     */
    private String displayName;

    /**
     * The email of the user
     */
    private String email;

    /**
     * The role of the user
     */
    private UserRole role;

    /**
     * List of appointment ids that the user has
     */
    private List<String> appointments;

}

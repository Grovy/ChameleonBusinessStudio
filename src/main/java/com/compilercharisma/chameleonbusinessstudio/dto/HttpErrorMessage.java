package com.compilercharisma.chameleonbusinessstudio.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpErrorMessage {

    /**
     * Code error of the error
     */
    private String code;

    /**
     * Error message
     */
    private String message;

}

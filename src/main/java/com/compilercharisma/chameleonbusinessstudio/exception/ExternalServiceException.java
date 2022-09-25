package com.compilercharisma.chameleonbusinessstudio.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends RuntimeException {

    private String message;
    private HttpStatus httpStatus;

    public ExternalServiceException(String message, HttpStatus httpStatus){
        super(message);
    }

    public ExternalServiceException(String message){
        super(message);
    }
}

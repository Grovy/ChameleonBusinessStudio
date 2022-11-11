package com.compilercharisma.chameleonbusinessstudio.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    private String message;
    private HttpStatus httpStatus;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
        this.httpStatus = HttpStatus.NOT_FOUND;

    }

    public String getMessage(){
        return message;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }
}

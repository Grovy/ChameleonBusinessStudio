package com.compilercharisma.chameleonbusinessstudio.exception;

import org.springframework.http.HttpStatus;

public class AppointmentNotFound extends RuntimeException {

    public static final long serialVersionUID = 1L;

    private String message;
    private HttpStatus httpStatus;

    public AppointmentNotFound() {
    }

    public AppointmentNotFound(String message) {
        super(message);
        this.message = message;
        this.httpStatus = HttpStatus.BAD_REQUEST;

    }

    public String getMessage(){
        return message;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }
}

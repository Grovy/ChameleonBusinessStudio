package com.compilercharisma.chameleonbusinessstudio.controller;

import com.compilercharisma.chameleonbusinessstudio.dto.HttpErrorMessage;
import com.compilercharisma.chameleonbusinessstudio.entity.AppointmentEntity;
import com.compilercharisma.chameleonbusinessstudio.exception.ExternalServiceException;
import com.compilercharisma.chameleonbusinessstudio.exception.InvalidAppointmentException;
import com.compilercharisma.chameleonbusinessstudio.exception.NoUserLoggedInException;
import com.compilercharisma.chameleonbusinessstudio.exception.UserNotRegisteredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final String RESOURCE_NOT_FOUND_MESSAGE = "error.message.resourceNotFound";
    public static final String MALFORMED_REQUEST_MESSAGE = "error.message.malformedRequest";
    public static final String ALREADY_EXISTS_MESSAGE = "error.message.alreadyExists";

    @ExceptionHandler(NoUserLoggedInException.class)
    public ResponseEntity<HttpErrorMessage> handleNoLoggedInUser(NoUserLoggedInException ex) {
        log.error(ex.getMessage(), ex);
        var response = HttpErrorMessage.builder()
                .message(ex.getMessage())
                .code(RESOURCE_NOT_FOUND_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotRegisteredException.class)
    public ResponseEntity<HttpErrorMessage> handleUserNotFound(UserNotRegisteredException ex) {
        log.error(ex.getMessage(), ex);
        var response = HttpErrorMessage.builder()
                .message(ex.getMessage())
                .code(RESOURCE_NOT_FOUND_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAppointmentException.class)
    public ResponseEntity<HttpErrorMessage> handleInvalidAppointment(InvalidAppointmentException ex) {
        log.error(ex.getMessage(), ex);
        var response = HttpErrorMessage.builder()
                .message(ex.getMessage())
                .code(MALFORMED_REQUEST_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<HttpErrorMessage> handleExternalError(ExternalServiceException ex) {
        log.error(ex.getMessage(), ex);
        var response = HttpErrorMessage.builder()
                .message(ex.getMessage())
                .code(ALREADY_EXISTS_MESSAGE)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}

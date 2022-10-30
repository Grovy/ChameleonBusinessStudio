package com.compilercharisma.chameleonbusinessstudio.controller;

import com.compilercharisma.chameleonbusinessstudio.exception.NoUserLoggedInException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public

    @ExceptionHandler(NoUserLoggedInException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<> handleNoLoggedInUser(NoUserLoggedInException ex){
        log.error(ex.getMessage(), ex);
        return ;
    }

}

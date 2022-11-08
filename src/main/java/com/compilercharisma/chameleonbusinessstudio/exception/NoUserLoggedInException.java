package com.compilercharisma.chameleonbusinessstudio.exception;

public class NoUserLoggedInException extends RuntimeException {
    public static final long serialVersionUID = 1L;
    
    public NoUserLoggedInException() {}

    public NoUserLoggedInException(String msg) {
        super(msg);
    }
}

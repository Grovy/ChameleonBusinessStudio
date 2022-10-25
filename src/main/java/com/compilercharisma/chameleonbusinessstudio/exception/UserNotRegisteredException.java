package com.compilercharisma.chameleonbusinessstudio.exception;

public class UserNotRegisteredException extends RuntimeException {
    public static final long serialVersionUID = 1L;
    
    public UserNotRegisteredException() {
    }

    public UserNotRegisteredException(String msg) {
        super(msg);
    }
}

package com.compilercharisma.chameleonbusinessstudio.exception;

/**
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class UserNotRegisteredException extends RuntimeException {
    public UserNotRegisteredException() {
    }

    public UserNotRegisteredException(String msg) {
        super(msg);
    }
}

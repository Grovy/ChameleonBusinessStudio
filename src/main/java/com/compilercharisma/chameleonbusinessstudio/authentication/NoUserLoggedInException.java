package com.compilercharisma.chameleonbusinessstudio.authentication;

/**
 *
 * @author Matt Crow <mattcrow19@gmail.com>
 */
public class NoUserLoggedInException extends RuntimeException {
    public NoUserLoggedInException() {}

    public NoUserLoggedInException(String msg) {
        super(msg);
    }
}

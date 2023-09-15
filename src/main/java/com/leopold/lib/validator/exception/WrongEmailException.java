package com.leopold.lib.validator.exception;

public class WrongEmailException extends  RuntimeException {
    public WrongEmailException() {
        super("The email has wrong format");
    }
}

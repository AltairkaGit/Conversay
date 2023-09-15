package com.leopold.lib.validator.exception;

public class LessMinLengthException extends  RuntimeException {
    public LessMinLengthException(int len) {
        super("Length is ess than min length, min len is: " + len);
    }
}

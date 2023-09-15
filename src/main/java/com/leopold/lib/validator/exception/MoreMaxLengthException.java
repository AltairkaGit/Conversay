package com.leopold.lib.validator.exception;

public class MoreMaxLengthException extends  RuntimeException {
    public  MoreMaxLengthException(int len) {
        super("More than max length, max len is: " + len);
    }
}

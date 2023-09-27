package com.leopold.lib.validator;

public interface Validator<T> {
    public void validate(String field, T t);
}

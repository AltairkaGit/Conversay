package com.leopold.lib.validator.impl;

import com.leopold.lib.ResponsibilityChain;
import com.leopold.lib.validator.Validator;

public class ValidatorImpl<T> implements Validator<T> {
    private ResponsibilityChain<T> chain;

    public ValidatorImpl(ResponsibilityChain<T> chain) {
        this.chain = chain;
    }

    @Override
    public void validate(T t) {
        chain.process(t);
    }
}

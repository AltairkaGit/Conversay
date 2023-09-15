package com.leopold.lib.validator.impl;

import com.leopold.lib.validator.AbstractValidationChain;
import com.leopold.lib.validator.exception.LessMinLengthException;

public class MinLengthValidation extends AbstractValidationChain<String> {
    private final int minLength;

    public MinLengthValidation(int minLength) {
        this.minLength = minLength;
    }

    @Override
    public void process(String s) {
        if (s.length() < minLength) throw new LessMinLengthException(minLength);
        else if (chain != null) chain.process(s);
    }
}

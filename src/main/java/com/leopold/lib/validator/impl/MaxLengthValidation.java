package com.leopold.lib.validator.impl;

import com.leopold.lib.validator.AbstractValidationChain;
import com.leopold.lib.validator.exception.MoreMaxLengthException;

public class MaxLengthValidation extends AbstractValidationChain<String> {
    private final int maxLength;

    public MaxLengthValidation(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void process(String field, String s) {
        if (s.length() > maxLength) throw new MoreMaxLengthException(field, maxLength);
        else if (chain != null) chain.process(field, s);
    }
}

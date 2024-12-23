package com.leopold.lib;

public interface ResponsibilityChain<T> {
    ResponsibilityChain<T> setNextChain(ResponsibilityChain<T> nextChain);
    void process(String field, T t);
}

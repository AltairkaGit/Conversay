package com.leopold.lib.compress;

import java.util.HashMap;
import java.util.Map;

import static com.leopold.lib.compress.LZWConfig.DICT_SIZE;
import static com.leopold.lib.compress.LZWConfig.START_CODE;

public class LZWDecoder {
    public static String decode(String s) {
        Map<Integer, String> dict = new HashMap<>();
        char[] data = (s).toCharArray();
        char currChar = data[0];
        String oldPhrase = String.valueOf(currChar);
        StringBuilder out = new StringBuilder();

        out.append(currChar);
        int code = START_CODE;
        String phrase;

        for (int i = 1; i < data.length; i++) {
            int currCode = (int) data[i];
            if (currCode < START_CODE) {
                phrase = String.valueOf(data[i]);
            } else {
                phrase = dict.containsKey(currCode) ? dict.get(currCode) : (oldPhrase + currChar);
            }
            out.append(phrase);
            currChar = phrase.charAt(0);
            if (dict.size() < DICT_SIZE) {
                dict.put(code, oldPhrase + currChar);
                code++;
            }
            oldPhrase = phrase;
        }

        return out.toString();
    }
}

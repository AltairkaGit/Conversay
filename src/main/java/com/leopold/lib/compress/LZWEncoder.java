package com.leopold.lib.compress;

import java.util.HashMap;
import java.util.Map;

import static com.leopold.lib.compress.LZWConfig.DICT_SIZE;
import static com.leopold.lib.compress.LZWConfig.START_CODE;

public class LZWEncoder {
    public static String encode(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }

        Map<String, Integer> dict = new HashMap<>();
        char[] data = (s).toCharArray();
        StringBuilder out = new StringBuilder();
        StringBuilder phrase = new StringBuilder();
        int code = START_CODE;

        phrase.append(data[0]);
        for (int i = 1; i < data.length; i++) {
            char currChar = data[i];
            String phrasePlusChar = phrase.toString() + currChar;
            if (dict.containsKey(phrasePlusChar)) {
                phrase.append(currChar);
            } else {
                out.append(phrase.length() > 1 ? (char)(int)dict.get(phrase.toString()) : phrase.charAt(0));
                if (dict.size() < DICT_SIZE) {
                    dict.put(phrasePlusChar, code);
                    code++;
                }
                phrase.setLength(0);
                phrase.append(currChar);
            }
        }
        out.append(phrase.length() > 1 ? (char)(int)dict.get(phrase.toString()) : phrase.charAt(0));
        System.out.println("encoder, out, " + out);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < out.length(); i++) {
            result.append((char) out.charAt(i));
        }

        return result.toString();
    }
}

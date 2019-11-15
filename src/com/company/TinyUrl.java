package com.company;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TinyUrl {
    private static Random rd = new Random();
    private static Map<Integer, String> urlToIndex = new HashMap<>();
    private static Map<Integer, Character> intToChar = new HashMap<>();
    private static Map<Character, Integer> charToInt = new HashMap<>();

    //Use 0-9, a-z => 36 chars in the shortened Urls.
    static {
        for(int i=0; i<26; i++) {
            intToChar.put(i, (char)('a'+i));
            charToInt.put((char)('a'+i), i);
        }
        for(int i=0; i<10; i++) {
            intToChar.put(26+i, (char)('0'+i));
            charToInt.put((char)('0'+i), 26+i);
        }
    }
    // Encodes a URL to a shortened URL.
    public String encode(String longUrl) {
        int index = 0; //Important: get positive interger only
        while(urlToIndex.containsKey(index)) {
            index = rd.nextInt() & Integer.MAX_VALUE;
        }
        urlToIndex.put(index, longUrl);

        String shortUrl = "";
        while(index>0) {
            shortUrl += intToChar.get(index%36);
            index = index/36;
        }

        return shortUrl;
    }

    // Decodes a shortened URL to its original URL.
    public String decode(String shortUrl) {
        int index = 0;
        char[] cArray = shortUrl.toCharArray();

        for(int i=0; i<cArray.length; i++) {
            index += charToInt.get(cArray[i])*(Math.pow(36, i));
        }
        return urlToIndex.get(index);
    }
}

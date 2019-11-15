package com.company;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Lc340_LongestSubstringWithKDistinctCharacters {
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        if (s == null || s.length() == 0 || k == 0) {
            return 0;
        } else if (s.length() < k) {
            return s.length();
        }

        Map<Character, Integer> lastOccur = new HashMap<>();
        int left = 0;
        int right = 0;
        int maxLen = 0;

        char[] sArray = s.toCharArray();
        while (right < s.length()) {
            char curChar = sArray[right];

            //If never seen this char in window before and already reached k
            if (lastOccur.size() == k && !lastOccur.containsKey(curChar)) {
                //Remove left most charater from window (by moving left pointer)
                int removeIndex = right;

                for (Integer lastSeen : lastOccur.values()) {
                    removeIndex = Math.min(removeIndex, lastSeen);
                }

                left = removeIndex + 1;
                lastOccur.remove(sArray[removeIndex]);
            }

            lastOccur.put(curChar, right);
            maxLen = Math.max(maxLen, right - left + 1);
            right++;
        }

        return maxLen;
    }
}

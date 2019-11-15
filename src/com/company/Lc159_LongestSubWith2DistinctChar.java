package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Lc159_LongestSubWith2DistinctChar {
        public int lengthOfLongestSubstringTwoDistinct(String s) {
            if (s == null || s.isEmpty()) {
                return 0;
            }

            //Keep a window, with lastAIndex, lastBIndex
            int maxWindowSize = 0;

            char[] cArray = s.toCharArray();
            int left = 0;
            int right = 0;

            List<Character> cExists = new ArrayList<>();
            Map<Character, Integer> cFirstIndex = new HashMap<>();

            Character prev = null;

            while (right < s.length()) {
                Character cur = cArray[right];

                if (!cur.equals(prev)) {
                    //New interval for existing cur
                    if (cExists.contains(cur)) {
                        cFirstIndex.put(cur, right);
                    }
                    //New character but set not full
                    else if (cExists.size() < 2) {
                        cExists.add(cur);
                        cFirstIndex.put(cur, right);
                    }
                    //New character and set is full
                    else {
                        //cur not in current window, remove the charater farthest to the right.
                        Integer removeInd = cFirstIndex.get(cExists.get(0)) < cFirstIndex.get(cExists.get(1)) ? 0 : 1;
                        Character cToRemove = cExists.get(removeInd);

                        cFirstIndex.remove(cToRemove);
                        cExists.remove(cToRemove);

                        left = cFirstIndex.get(cExists.get(0));
                        cFirstIndex.put(cur, right);
                        cExists.add(cur);
                    }
                }

                maxWindowSize = Math.max(maxWindowSize, right - left + 1);
                prev = cur;
                right++;
            }

            return maxWindowSize;
        }

}

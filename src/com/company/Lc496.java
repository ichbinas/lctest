package com.company;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Lc496 {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> greaterThanCur = new HashMap<>(); //numbers in nums2, and their next bigger number

        for (int index = 0; index < nums2.length; index++) {
            int target = nums2[index];
            for (int index2 = index + 1; index2 < nums2.length; index2++) {
                if (target < nums2[index2]) {
                    greaterThanCur.put (target, nums2[index]);
                    break;
                }
            }
        }

        return Arrays.stream(nums1).map(e->greaterThanCur.get(e)).toArray();
    }
}

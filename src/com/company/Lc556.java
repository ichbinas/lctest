package com.company;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lc556 {
    //Split the number into integer arrays. The problem turns to:
    //for any digits[i], is there a j>i, where digits[j] > digits[i]
    public int nextGreaterElement(int n) {
        List<Integer> digits = new ArrayList<>();

        while(n != 0) {
            int curDigit = n%10;
            n /= 10;
            digits.add(curDigit);
        }

        int i = 0;
        //search from right: for digits[i], what is smallest digits[j] where digits[j] > digits[i] and j>i
        for (; i < digits.size(); i++) {
            int target = digits.get(i);
            int min = 10;
            int minIndex = -1;

            for (int j = i-1; j >= 0; j--) {
                if (digits.get(j) > target && digits.get(j) < min) {
                    min = digits.get(j);
                    minIndex = j;
                }
            }
            //If no min found, continue; otherwise, we already found the smallest bigger number
            if (min != 10) {
                int tmp = digits.get(i);
                digits.set(i, min);
                digits.set(minIndex, tmp);
                break;
            }
        }

        if(i == digits.size()) {
            return -1;
        } else {
            long rtn = 0;
            List<Integer> subList = digits.subList(0, i);
            Collections.sort(subList, Collections.reverseOrder());

            for(int j=0; j<subList.size(); j++) {
                rtn += subList.get(j)*(Math.pow(10, j));
            }
            for(int j=i; j<digits.size(); j++) {
                rtn += digits.get(j)*(Math.pow(10, j));
            }
            return rtn > Integer.MAX_VALUE ? -1:Math.toIntExact(rtn);
        }
    }
}

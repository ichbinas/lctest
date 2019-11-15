package com.company;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AnagramMappings {
    public int[] anagramMappings(int[] A, int[] B) {
        Map<Integer, List<Integer>> aValueToIndex = new HashMap<>();
        for(int i=0; i< A.length; i++) {
            List<Integer> indexList;
            if(aValueToIndex.containsKey(A[i])) {
                indexList = aValueToIndex.get(A[i]);

            } else {
                indexList = new LinkedList<>();
            }
            indexList.add(i);
            aValueToIndex.put(A[i], indexList);
        }

        int[] rtn = new int[B.length];
        for(int i=0; i<B.length; i++) {
            rtn[i]=aValueToIndex.get(B[i]).get(0);
            aValueToIndex.get(B[i]).remove(0);
        }
        return rtn;
    }
}

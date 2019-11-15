package com.company;

public class Lc768_MaxChunkII {
    public int maxChunksToSorted(int[] arr) {
        if (arr == null || arr.length == 0) {
            return 0;
        }

        int arraySize = arr.length;
        int[] maxLeft = new int[arraySize];
        int[] minRight = new int[arraySize];

        int curMax = arr[0];
        for (int i = 0; i < arraySize; i++) {
            curMax = Math.max(arr[i], curMax);
            maxLeft[i] = curMax;
        }

        int curMin = arr[arraySize - 1];
        for (int i = arraySize - 1; i >= 0; i--) {
            curMin = Math.min(arr[i], curMin);
            minRight[i] = curMin;
        }

        int count = 1;
        for (int i = 1 ; i < arraySize; i++) {
            if (maxLeft[i-1] <= minRight[i]) {
                count++;
            }
        }

        return count;
    }
}

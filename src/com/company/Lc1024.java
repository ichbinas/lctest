package com.company;

import java.util.Arrays;
import java.util.Comparator;

public class Lc1024 {
    public int videoStitching(int[][] clips, int T) {
        // Sort clips, [][0] ascending, [][1] descending.
        sortTwoDimension(clips);

        int[] dp = new int[T+1];

        if (clips[0][0] != 0) {
            return -1;
        }

        dp[0] = 1;
        int head = clips[0][0];
        int tail = clips[0][1];
        int clipIndex = 0;

        // dp[i] = dp[i] if last dp[i-1].tail >= i;
        // dp[i] = dp[i-1] + 1 if dp[i-1].tail < i;
        for (int i = 1; i <= T; i++) {
            if (tail >= i) {
                dp[i] = dp[i-1];
            } else {
                // search in clips for the first one [i][0] != head
                clipIndex++;
                while (clipIndex < clips.length &&
                        (clips[clipIndex][0] <= head || clips[clipIndex][1] <= tail)) {
                    clipIndex++;
                }

                if (clipIndex < clips.length) {
                    head = clips[clipIndex][0];
                    tail = clips[clipIndex][1];
                } else {
                    return -1;
                }

                dp[i] = dp[i-1] + 1;
            }
        }

        System.out.println("dp[T]: " + dp[T]);
        return dp[T];
    }

    private void sortTwoDimension(int[][] clips) {
        Arrays.sort(clips, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                int[] one = (int[]) o1;
                int[] two = (int[]) o2;

                if (one[0] > two[0]) {
                    return 1;
                } else if (one[0] < two[0]) {
                    return -1;
                } else {
                    return (two[1] - one[1]);
                }
            }
        });
    }
}

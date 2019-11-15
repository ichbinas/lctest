package com.company;

public class Lc910_SmallestRangeII {
    public int smallestRangeII(int[] A, int K) {
        //With current value i, curMin, curMax
        //1. try new = i+K;
        //   new > max, newdiff1 = new-curMin;
        //   new < min, min = new, continue;
        //   else, do nothing, continue (we can alwasy i+K to keep using current curMin, curMax)
        //2. try new = i-k;
        //   new > max, max = new, continue;
        //   new < min, newdiff2 = curMax-new;
        //   else, do nothing, continue;
        //Now, should I +K or -K?
        //if newdiff1 < newdiff2 (=> 2i<curMax+curMin), max = new;
        //else (newdiff1 >= newdiff2), min = new;
        if (A.length <= 1) {
            return 0;
        }
        int curMin = A[0]-K;
        int curMax = A[0]-K;

        int[] startMin = findMinMax(A, curMin, curMax, K);

        curMin = A[0]+K;
        curMax = A[0]+K;
        int[] startMax = findMinMax(A, curMin, curMax, K);

        return Math.min(startMin[1]-startMin[0], startMax[1]-startMax[0]);
    }

    private int[] findMinMax(int[] A, int curMin, int curMax, int K) {
        for (int i=1; i< A.length; i++) {
            int newA = A[i]+K;
            if (newA < curMin) {
                curMin = newA;
                continue;
            } else if (newA >= curMin && newA <= curMax) {
                continue;
            }

            newA = A[i]-K;
            if (newA > curMax) {
                curMax = newA;
                continue;
            } else if (newA >= curMin && newA <= curMax) {
                continue;
            }

            if(A[i] < (float)(curMax+curMin)/2) {
                curMax = A[i]+K;
            } else {
                curMin = A[i]-K;
            }
        }

        return new int[]{curMin, curMax};
    }
}

package com.company;

public class Lc849_MaxDistToClosestPerson {
    public int maxDistToClosest(int[] seats) {
        //For each emptySlot window [x,y], calculate minDist
        int left = 0;
        int right = 0;
        int globalMaxDist = 0;
        int maxIndex = seats.length - 1;

        while (left < seats.length && right < seats.length) {
            while (left < seats.length && seats[left] == 1) left++;
            right = left;
            while (right+1 < seats.length && seats[right+1] == 0) right++;

            if (right < seats.length) {
                int locDist = getDist(left, right, maxIndex);
                if (locDist > globalMaxDist) {
                    globalMaxDist = locDist;
                }
            }
            left = right + 1;
        }

        return globalMaxDist;
    }

    private int getDist(int left, int right, int maxIndex) {
        if (left == 0 || right == maxIndex) {
            return right - left + 1;
        } else {
            return ((right - left) >> 1) + 1;
        }
    }
}

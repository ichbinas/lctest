package com.company;

import java.util.Random;
import java.util.TreeMap;

public class Lc497_RandomPointsInRects {
    int totalArea;
    int[] rectBnd;
    int[][] rects;
    Random randomSeed;

    public Lc497_RandomPointsInRects(int[][] rects) {
        totalArea = 0;
        rectBnd = new int[rects.length + 1];
        rectBnd[0] = 0;

        this.rects = rects;

        //Map each rects's area to a number, add up these numbers
        for (int i = 0 ; i < rects.length; i++) {
            int area = (rects[i][2] - rects[i][0] + 1) * (rects[i][3] - rects[i][1] +1);
            rectBnd[i + 1] = rectBnd[i] + area;
        }

        totalArea = rectBnd[rectBnd.length - 1];
        randomSeed = new Random();
    }

    public int[] pick() {
        int module = Math.abs(randomSeed.nextInt(totalArea));

        int index = searchRectIndex(module) - 1; //Bnd index - 1 => rectx index
        if (index >= 0 && index < rects.length) {
            int[] rtn = convertNumberToIndex(module - rectBnd[index], index);
            boolean isValid = validateRtn(rtn);
            return rtn;
        }
        else return new int[]{};
    }

    private boolean validateRtn(int[] num) {
        for (int i = 0; i < rects.length; i++) {
            if (num[0] >= rects[i][0] && num[0] <= rects[i][2] &&
                    num[1] >= rects[i][1] && num[1] <= rects[i][3])
            return true;
        }
        return false;
    }

    //search this num in rectBnd and return the index of element which covers this number
    private int searchRectIndex (int num) {
        num++; // turn the 0~totalArea - 1 num to 1 ~ totalArea.
        int left = 1;
        int right = rectBnd.length -1;
        int index;

        while (left <= right) {
            index = (right + left) >> 1;
            if (num > rectBnd[index - 1] && num <= rectBnd[index]) {
                return index;
            } else if (num <= rectBnd[index -1]) {
                right = index -1;
            } else if (num > rectBnd[index]) {
                left = index + 1;
            }
        }
        return -1;
    }

    private int[] convertNumberToIndex (int num, int i) {
        int rowWidth = rects[i][3] - rects[i][1] + 1;

        int row = rects[i][0] + num / rowWidth;
        int col = rects[i][1] + num % rowWidth;

        return new int[] {row, col};
    }
}

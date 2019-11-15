package com.company;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Lc497_RandomPointsInRectsTree {
    TreeMap<Integer, Integer> map;
    int[][] rects;
    int totalArea;
    Random r;

    public Lc497_RandomPointsInRectsTree(int[][] rects) {
        this.rects = rects;
        r = new Random();
        map = new TreeMap<>();
        totalArea = 0;

        for (int i = 0; i < rects.length; i++) {
            totalArea += (rects[i][2] - rects[i][0] + 1) * (rects[i][3] - rects[i][1] + 1);
            map.put (totalArea, i);
        }

    }

    public int[] pick() {
        int num = Math.abs(r.nextInt(totalArea)) + 1;
        //Map.Entry entry = map.ceilingEntry();
        int rectIndex = map.ceilingEntry(num).getValue();

            int rowWidth = rects[rectIndex][3] - rects[rectIndex][1] + 1;
        int col = rects[rectIndex][1] + (num - 1) % rowWidth;
        int row = rects[rectIndex][0] + (num -1) / rowWidth;

        return new int[] {row, col};
    }
}

package com.company;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.PriorityQueue;

public class Lc901_1 {
    //Keep monotonic decreasing stack
    Deque<int[]> stack; //in[0] price, int[1] day
    int dayCount;

    public Lc901_1() {
        stack = new ArrayDeque<>();
        dayCount = 0;
    }

    public int next(int price) {
        dayCount++;

        //We only care about when higher price happened
        while (!stack.isEmpty() && stack.peekFirst()[0] <= price) {
            stack.pop();
        }
        int curTop = stack.peekFirst()[1];
        stack.push(new int[] {price, dayCount});
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        return stack.isEmpty() ? dayCount : dayCount - curTop;
    }
}

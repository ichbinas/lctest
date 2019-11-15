package com.company;

import java.util.ArrayDeque;
import java.util.Deque;

public class Lc255_verifyPreorder {
    public boolean verifyPreorder(int[] preorder) {
        int smallest = Integer.MIN_VALUE;

        // Keep a stack, if pop until current int < stack top
        // Eventually, the stack should be empty
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i : preorder) {
            if (i < smallest) {
                return false;
            }

            while (!stack.isEmpty() && stack.peek() < i) {
                stack.pop();
                smallest = i;
            }

            stack.push(i);
        }

        return true;
    }
}

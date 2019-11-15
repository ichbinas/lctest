package com.company;

import java.util.Map;
import java.util.TreeMap;

public class Lc731_MyCalenarII {
    TreeMap<Integer, Integer> bndryCnt; // {number:1} left boundary of a slot, {number:-1} right boundary of a slot

    public Lc731_MyCalenarII() {
        bndryCnt = new TreeMap();
    }

    //If triple/n booking happens, when we traverse from left to right, we should encounter 3/n left boundaries
    //Unclosed intervals
    public boolean book(int start, int end) {
        return false;
    }
}

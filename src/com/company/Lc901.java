package com.company;

import javafx.util.Pair;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Lc901 {
    ArrayList<Integer> dp;
    ArrayList<Integer> prices;

    Deque<Map.Entry<Integer, Integer>> stack; // <Price, nextValue>

    public Lc901() {
        dp = new ArrayList<>();
        prices = new ArrayList<> ();

        stack = new ArrayDeque<>();
    }

    //Two cases:
    // 1. If prices[cur] < prices[cur-1], dp[cur]=1
    // 2. If prices[cur] >= prices[cur-1], search the first i where prices[i]>price[cur]
    //    Tricky, like KMP, always jump to prices[cur-1-dp[cur-1]] as prices[cur-1-dp[cur-1]+1]...prices[cur-1] are <= prices[cur-1]
    public int next(int price) {
        Integer newDp = 1;

        int index = prices.size()-1;

        while (index>=0 && prices.get(index) < price) {
            newDp += dp.get(index);
            index -= dp.get(index);
        }

        prices.add(price);
        dp.add(newDp);
        return newDp;
    }

    //Memory efficient solution: There is no need to record all dp. always remove those price < cuPrice.
    //Worst case: all elements in descending order [10,9,8,7,6]
    public int next_memEfficient(int price) {
        Integer newDp = 1;

        while(!stack.isEmpty() && stack.peek().getKey() < price) {
            newDp += stack.pop().getValue();
        }

        stack.push(new HashMap.SimpleEntry<>(price, newDp));

        return newDp;
    }
}

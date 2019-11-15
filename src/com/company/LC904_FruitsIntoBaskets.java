package com.company;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

public class LC904_FruitsIntoBaskets {
    class FruitCount{
        Integer type;
        Integer count;

        public FruitCount(Integer fruitType, Integer fruitCount) {
            this.count = fruitCount;
            this.type = fruitType;
        }

        public Integer getFruitType() {return type;}
        public Integer getFruitCount() {return count;}
        public void addFruitCount() {count++;}
    }

    public int totalFruit(int[] tree) {
        if (tree.length == 0) {
            return 0;
        }

        return totalFruitWithNBaskets(tree, 2);
    }

    public int totalFruitWithNBaskets(int[] tree, int N) {
        if (tree.length<N) {
            return tree.length;
        }

        Deque<FruitCount> fruitWindow = new ArrayDeque<>(); //{a, 3}-> {b, 1} -> {a,2}.
        Set<Integer> fruitSet = new HashSet<>(); //mark all fruits already in window

        int globalMax = 1;
        int curWindowSize = 1;
        fruitWindow.addLast(new FruitCount(tree[0], 1));
        fruitSet.add(tree[0]);

        for (int i=1; i<tree.length; i++) {
            int tailFruit = fruitWindow.getLast().getFruitType();

            if (tailFruit == tree[i]) {
                fruitWindow.peekLast().addFruitCount();
                curWindowSize++;
            } else if (fruitSet.contains(tree[i])) {
                fruitWindow.addLast(new FruitCount(tree[i], 1));
                curWindowSize++;
            }
            else {
                //Encounter different type: 1. Add newF to type; 2. check if exceeds N
                //If more than N, remove 1 or 2 fruits from left.
                if (fruitSet.size() >= N) {
                    if (tailFruit == fruitWindow.getFirst().getFruitType()) {
                        curWindowSize -= fruitWindow.getFirst().getFruitCount();
                        fruitWindow.removeFirst();
                    }
                    if (fruitWindow.size() > 0) {
                        curWindowSize -= fruitWindow.getFirst().getFruitCount();
                        fruitSet.remove(fruitWindow.getFirst().getFruitType());
                        fruitWindow.removeFirst();
                    }
                }

                fruitSet.add(tree[i]);
                fruitWindow.addLast(new FruitCount(tree[i], 1));
                curWindowSize++;
            }
            globalMax = Math.max(curWindowSize, globalMax);
        }

        return globalMax;
    }
}

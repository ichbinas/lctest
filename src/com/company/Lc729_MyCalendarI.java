package com.company;

import java.util.SortedMap;
import java.util.TreeMap;

public class Lc729_MyCalendarI {
    class intNode {
        public Integer left;
        public Integer right;
        public intNode leftNode;
        public intNode rightNode;

        public intNode(Integer left, Integer right, intNode leftNode, intNode rightNode) {
            this.left = left; this.right = right; this.leftNode = leftNode; this.rightNode = rightNode;
        }
    }

    intNode root;
    TreeMap<Integer, Integer> occupiedIntervals;

    public Lc729_MyCalendarI() {
        occupiedIntervals = new TreeMap<>();
        root = null;
    }

    public boolean book(int start, int end) {
        Integer prev = occupiedIntervals.floorKey(start);
        Integer next = occupiedIntervals.ceilingKey(start);

        if ((prev != null && occupiedIntervals.get(prev) <= start) &&
                (next != null && occupiedIntervals.get(next) >= end)) {
            occupiedIntervals.put(start, end);
            return true;
        }

        return false;
    }

    public boolean book_binaray(int start, int end) {
        if (root == null) {
            root = new intNode(start, end, null, null);
            return true;
        } else {
            return insert(start, end, root);
        }
    }

    private boolean insert(int start, int end, intNode node) {
        if (start>=node.right) {
            if (node.rightNode != null) {
                return insert(start, end, node.rightNode);
            } else {
                node.rightNode = new intNode(start, end, null, null);
                return true;
            }
        } else if (end<=node.left) {
            if (node.leftNode != null) {
                return insert(start, end, node.leftNode);
            } else {
                node.leftNode = new intNode(start,end, null, null);
                return true;
            }
        } else {
            return false;
        }
    }
}

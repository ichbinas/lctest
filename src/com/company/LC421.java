package com.company;

public class LC421 {
    class BinaryTree {
        public BinaryTree left;
        public BinaryTree right;

        public BinaryTree() {
        }
    }

    public int findMaximumXOR(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }

        BinaryTree root = new BinaryTree();

        //Build tree: root -> leaf; leftmost bit -> right most bit ever appeared.
        BinaryTree cur = root;
        for (int curNum : nums) {
            cur = root;
            for (int i = 31; i >= 0; i--) {
                int curBit = curNum & (1<<i);
                if (curBit == 0) {
                    if (cur.left == null) {
                        cur.left = new BinaryTree();
                    }
                    cur = cur.left;
                } else if (curBit != 0) {
                    if (cur.right == null){
                        cur.right = new BinaryTree();
                    }
                    cur = cur.right;
                }
            }
        }

        //Search the tree: for every num, 31:0, if the ith bit in one position is "bit":
        // if "bit^1" node exists, search from "bit^1" branch after that, add max we can get from ith
        // if not, search from "bit",  meaning no number will give you different bit for the ith bit
        int maxGlobal = 0;
        for (int curNum : nums) {
            cur = root;
            int maxLocal = 0;
            for (int i = 31; i >= 0; i--) {
                int curBit = curNum & (1<<i);
                if (curBit == 0 && cur.right != null) {
                    cur = cur.right;
                    maxLocal += (1<<i);
                } else if (curBit !=0 && cur.left != null) {
                    cur = cur.left;
                    maxLocal += (1<<i);
                } else {
                    cur = (curBit == 0) ? cur.left:cur.right;
                }
            }
            maxGlobal = Math.max(maxLocal, maxGlobal);
        }

        return maxGlobal;
    }
}

package com.company;

import java.util.ArrayList;
import java.util.List;

public class NumOfIslands {
    class UnionFind{
        public int count; //number of "islands"
        int[] root; //root is the index hash of a cell. if root[i] is cell[x][y], root[i] = x*width+y;
        int[] rank;

        public UnionFind(int n) {
            count=0;
            root = new int[n];
            rank = new int[n];
            for(int i = 0; i<n; i++) {
                root[i] = -1;
                rank[i] = 0;
            }
        }

        //Set default root for a cell
        public void setRoot(int n) {
            root[n] = n;
            count++;
        }

        public void union(int n1, int n2) {
            int root1 = findRoot(n1);
            int root2 = findRoot(n2);

            if(root1 != root2) {
                if(rank[root1]>=rank[root2]) {
                    root[root2] = root1;
                    rank[root1]++;
                }
                else{
                    root[root1] = root2;
                    rank[root2]++;
                }
                count--;
            }
            //if root1==root2, they are already in one island. do nothing
        }

        public boolean isValidIsland(int n) {
            return root[n]>=0;
        }

        private int findRoot(int n) {
            while(root[n] != n) n = root[n];

            return root[n];
        }
    }

    public List<Integer> numIslands2(int m, int n, int[][] positions) {
        UnionFind uf = new UnionFind(m*n);
        List<Integer> rtnList = new ArrayList<Integer>();

        for(int[] pos:positions){
            int row = pos[0];
            int col = pos[1];
            int cur = row*n+col;

            //mark the position as an isLand, root is itself
            uf.setRoot(cur);

            //if any of neighbor cells are "1", then union
            int[] islandNeighbors = {-1,-1,-1,-1};

            if(row-1 >=0) islandNeighbors[0] = (row-1)*n + col;
            if(row+1 < m) islandNeighbors[1] = (row+1)*n + col;
            if(col-1 >=0) islandNeighbors[2] = row*n + col-1;
            if(col+1 < n) islandNeighbors[3] = row*n + col+1;

            for(int i:islandNeighbors) {
                if(i>=0 && uf.isValidIsland(i)){
                    uf.union(i, cur);
                }
            }
            rtnList.add(uf.count);
        }
        return rtnList;
    }
}

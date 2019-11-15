package com.company;

import java.util.ArrayList;
import java.util.List;

public class Lc216_CombinationSum3 {
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> rtnList = new ArrayList<>();
        if (k > n || 9*k < n) {
            return rtnList;
        }

        permutationHelper(rtnList, new ArrayList<>(), 1, n, k);

        for (List<Integer> list : rtnList) {
            list.stream().mapToInt(Integer::intValue).min().getAsInt();
        }
        return rtnList;
    }

    private void permutationHelper(List<List<Integer>> rtnList, List<Integer> curList,
                                   int start, int target, int count) {
        if (count == 0) {
            return;
        }

        for (int i = start; i <= 9; i++) {
            curList.add(i);

            if (i == target && count == 1) {
                rtnList.add(new ArrayList<>(curList));
            }

            //Assuming we can reuse the same number
            permutationHelper(rtnList, curList, i+1, target - i, count - 1);

            curList.remove(curList.size() -1);
        }
    }
}

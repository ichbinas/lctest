package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {
    public static List<List<Integer>> get3Sum(int[] nums) {
        List<List<Integer>> rtnList = new ArrayList<>();
        int base = 0;
        int l, r;

        Arrays.sort(nums);

        while(base<(nums.length-2)){
            int expect = 0-nums[base];
            l = base+1;
            r = nums.length-1;

            while(l<r) {
                int curSum = nums[l]+nums[r];

                if(curSum < expect) {
                    l++;
                }
                else if(curSum > expect) {
                    r--;
                }
                else {
                    rtnList.add(Arrays.asList(nums[base],nums[l],nums[r]));
                    l++; //Important, always move index first
                    r--;
                    while(l<r && nums[l] == nums[l-1]) l++;
                    while(l<r && nums[r] == nums[r+1]) r--;
                }
            }
            base++;
            while(base<(nums.length-2) && nums[base] == nums[base-1]) base++;
        }
        return rtnList;
    }
}

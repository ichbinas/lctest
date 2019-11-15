package com.company;

import java.util.Random;

public class Lc215_KthLargest {
    public int findKthLargest(int[] nums, int k) {
        return findKthLargestBetween(nums, k, 0, nums.length-1);
    }

    private int findKthLargestBetween(int[] nums, int k, int start, int end) {
        int sentinelPos = partition(nums, start, end);
        Random r = new Random();
        r.nextInt(10000);
        //nums[sentinelPos] is the the (sentinelPos - start +1)th largest from start
        int leftSize = sentinelPos - start + 1;

        if (leftSize == k) {
            return nums[sentinelPos];
        } else if (leftSize < k) {
            //search right
            return findKthLargestBetween(nums, k-leftSize, sentinelPos+1, end);
        } else {
            //search left
            return findKthLargestBetween(nums, k, start, sentinelPos-1);
        }

    }

    //Return a index, left => no less than nums[end], right => smaller than nums[end]
    private int partition(int[] nums, int start, int end) {
        int left = start;
        int right = end;
        int pivot = nums[right];

        while (left < right) {
            //Find a number less than target so we can throw to the right
            while (left < right && nums[left] >= pivot) left++;
            if (left < right) {
                nums[right] = nums[left];
                right--;
            }

            while (left < right && nums[right] < pivot) right--;
            if (left < right) {
                nums[left] = nums[right];
                left++;
            }
        }
        nums[left] = pivot;
        return left;
    }
}

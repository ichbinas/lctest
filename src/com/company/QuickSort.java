package com.company;

public class QuickSort {
    public int findKthLargest(int[] nums, int k) {
        return findKth(nums, k, 0, nums.length-1);
    }

    //Find the kthe largest counting from l
    private int findKth(int[] nums, int k, int l, int r) {
        if(nums.length == 1)
            return nums[0];

        int sentinel = partition(nums, l, r);
        int leftsize = sentinel-l+1;

        if(leftsize == k)
            return nums[sentinel];
        else if(leftsize> k) {
            return findKth(nums, k, l, sentinel-1);
        }
        else {
            return findKth(nums, k-leftsize, sentinel+1, r);
        }
    }

    //return index. nums[<index]>nums[index], nums[>index]<nums[index]
    private int partition(int[] nums, int low, int high) {
        int pivot = nums[high];
        int l = low-1;
        int r = high;
        while(true) {
            while(nums[++l]>pivot);
            while(l<r && nums[--r]<pivot);
            if(l<r) {
                swap(nums, l, r);
            }
            else {
                swap(nums, l, high);
                return l;
            }
        }
    }

    private void swap(int[] nums, int l, int r) {
        int tmp = nums[l];
        nums[l] = nums[r];
        nums[r] = tmp;
    }
}

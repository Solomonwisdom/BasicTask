package com.whg.medium.sortcolors;

/**
 * Solution class
 *
 * problemId 75
 * @author wanghaogang
 * @date 2018/6/26
 */
public class Solution {

    private void swap(int[] nums, int a, int b) {
        int tmp = nums[a];
        nums[a] = nums[b];
        nums[b] = tmp;
    }

    public void sortColors(int[] nums) {
        int l = 0, mid = 0, r = nums.length - 1;
        while (mid <= r) {
            if (nums[mid] == 0) {
                swap(nums, l++, mid++);
            } else if (nums[mid] == 1) {
                ++mid;
            } else {
                swap(nums, mid, r--);
            }
        }
    }

    public static void main(String[] args) {
        int[] nums = {2, 0, 2, 1, 1, 0};
        new Solution().sortColors(nums);
        for (int x : nums) {
            System.out.print(x + ",");
        }
        System.out.println();
    }
}

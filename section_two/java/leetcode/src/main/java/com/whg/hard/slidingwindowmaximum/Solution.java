package com.whg.hard.slidingwindowmaximum;

/**
 * Solution class
 *
 * problemId 239
 * @author wanghaogang
 * @date 2018/6/18
 */
public class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return new int[]{};
        }
        int[] pQueue = new int[nums.length];
        int[] ans = new int[nums.length - k + 1];
        int l = 0, r = 0;
        for (int i = 0; i < nums.length; ++i) {
            if (i < k) {
                while (r > l && nums[pQueue[r - 1]] <= nums[i]) {
                    --r;
                }
                pQueue[r++] = i;
            } else {
                ans[i - k] = nums[pQueue[l]];
                if (pQueue[l] == i - k) {
                    ++l;
                }
                while (r > l && nums[pQueue[r - 1]] <= nums[i]) {
                    --r;
                }
                pQueue[r++] = i;
            }
        }
        ans[nums.length - k] = nums[pQueue[l]];
        return ans;
    }

    public static String arrayToString(int[] arr) {
        String s = "[";
        for (int x : arr) {
            s += x + ",";
        }
        return s + "]";
    }

    public static void main(String[] args) {
        int[] nums = new int[]{1, 3, -1, -3, 5, 3, 6, 7};
        int k = 3;
        System.out.println(arrayToString(new Solution().maxSlidingWindow(nums, k)));
        nums = new int[]{1, 3, 1, 2, 0, 5};
        System.out.println(arrayToString(new Solution().maxSlidingWindow(nums, k)));
    }
}

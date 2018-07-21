package com.whg.medium.targetsum;

/**
 * Solution class
 *
 * problemId 494
 * @author wanghaogang
 * @date 2018/6/28
 */
public class Solution {

    private int dfs(int[] nums, int S, int pos, int curS) {
        if (pos == nums.length) {
            if (curS == S) {
                return 1;
            } else {
                return 0;
            }
        }
        return dfs(nums, S, pos + 1, curS + nums[pos]) +
                dfs(nums, S, pos + 1, curS - nums[pos]);
    }

    public int findTargetSumWays(int[] nums, int S) {
        return dfs(nums, S, 0, 0);
    }

    public static void main(String[] args) {
        int[] nums = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        System.out.println(new Solution().findTargetSumWays(nums,3));
    }
}

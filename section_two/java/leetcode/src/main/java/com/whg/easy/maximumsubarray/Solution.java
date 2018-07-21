package com.whg.easy.maximumsubarray;

/**
 * Solution class
 *
 * problemId 53
 * @author wanghaogang
 * @date 2018/6/29
 */

class Solution {

    public int maxSubArray(int[] nums) {
        int min = 0, sum = 0;
        int ans = Integer.MIN_VALUE;
        for (int num : nums) {
            sum += num;
            ans = Math.max(ans, sum - min);
            min = Math.min(min, sum);
        }
        return ans;
    }

}

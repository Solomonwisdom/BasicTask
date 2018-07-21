package com.whg.easy.majorityelement;

import java.util.Arrays;

/**
 * Solution class
 *
 * problemId 169
 * @author wanghaogang
 * @date 2018/6/29
 */
public class Solution {

    public int majorityElement(int[] nums) {
        if (nums.length == 1) {
            return nums[0];
        }
        Arrays.sort(nums);
        int curCount = 1, ans = nums[0], target = nums.length / 2 + 1;
        for (int i = 1; i < nums.length; ++i) {
            if (nums[i] == nums[i - 1]) {
                ++curCount;
            } else {
                if (curCount >= target) {
                    ans = nums[i - 1];
                }
                curCount = 1;
            }
        }
        if (curCount >= target) {
            ans = nums[nums.length - 1];
        }
        return ans;
    }
}

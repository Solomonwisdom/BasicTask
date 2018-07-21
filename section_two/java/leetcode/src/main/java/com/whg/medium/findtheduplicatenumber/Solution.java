package com.whg.medium.findtheduplicatenumber;

/**
 * Solution class
 *
 * problemId 287
 * @author wanghaogang
 * @date 2018/6/28
 */
public class Solution {

    public int findDuplicate(int[] nums) {
        int oneStep = nums[0];
        int twoStep = nums[0];
        do {
            oneStep = nums[oneStep];
            twoStep = nums[nums[twoStep]];
        } while (twoStep!=oneStep);
        int pos1 = nums[0];
        while (pos1!=oneStep) {
            pos1 = nums[pos1];
            oneStep = nums[oneStep];
        }
        return pos1;
    }
}

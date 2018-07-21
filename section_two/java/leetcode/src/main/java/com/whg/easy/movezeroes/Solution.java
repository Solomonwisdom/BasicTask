package com.whg.easy.movezeroes;

/**
 * Solution class
 *
 * problemId 283
 * @author wanghaogang
 * @date 2018/6/29
 */

class Solution {

    public void moveZeroes(int[] nums) {
        int curIndex = 0;
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] != 0) {
                nums[curIndex++] = nums[i];
            }
        }
        for (; curIndex < nums.length; ++curIndex) {
            nums[curIndex] = 0;
        }
    }

}

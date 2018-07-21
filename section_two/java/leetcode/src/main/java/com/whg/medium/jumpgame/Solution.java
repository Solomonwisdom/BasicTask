package com.whg.medium.jumpgame;

/**
 * Solution class
 *
 * problemId 55
 * @author wanghaogang
 * @date 2018/6/24
 */
public class Solution {

    public boolean canJump(int[] nums) {
        int last = 0;
        for (int i=0; i<nums.length; ++i) {
            if(last>=i) {
                if(last<i+nums[i]) {
                    last = i+nums[i];
                }
            } else {
                break;
            }
        }
        return last>=nums.length-1;
    }
    public static void main(String[] args) {
        int[][] numss = {{2,3,1,1,4},{3,2,1,0,4},{2,1,3},{1,2,1,0,3},{1}};
        for (int[] nums: numss) {
            System.out.println(new Solution().canJump(nums));
        }
    }
}

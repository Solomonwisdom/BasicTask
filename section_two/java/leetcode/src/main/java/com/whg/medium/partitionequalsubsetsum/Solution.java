package com.whg.medium.partitionequalsubsetsum;

/**
 * Solution class
 *
 * problemId 416
 * @author wanghaogang
 * @date 2018/6/26
 */
public class Solution {

    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int x: nums) {
            sum += x;
        }
        if((sum&1)!=0) {
            return false;
        }
        sum/=2;
        boolean[] dp = new boolean[sum+1];
        dp[0]=true;
        for (int x:nums) {
            for (int i = sum;i>=x;--i) {
                if(dp[i-x]) {
                    dp[i]=true;
                }
            }
        }
        return dp[sum];
    }

    public static void main(String[] args) {
        int[][] numss = {{1, 5, 11, 5},{1, 2, 3, 5}};
        for (int[] nums: numss) {
            System.out.println(new Solution().canPartition(nums));
        }
    }
}

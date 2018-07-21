package com.whg.medium.besttimetobuyandsellstockwithcooldown;

/**
 * Solution class
 *
 * problemId 309
 * @author wanghaogang
 * @date 2018/6/27
 */
public class Solution {

    private int max(int a,int b) {
        return a>b?a:b;
    }

    public int maxProfit(int[] prices) {
        if (prices==null||prices.length==0) {
            return 0;
        }
        int[][] dp = new int[prices.length+1][2];
        dp[0][0]=-prices[0];
        for (int i=1;i<prices.length;++i) {
            dp[i][0]=max(dp[i-1][0],(i>=2?dp[i-2][1]:0)-prices[i]);
            dp[i][1]=max(dp[i-1][1],dp[i-1][0]+prices[i]);
        }
        return dp[prices.length-1][1];
    }

    public static void main(String[] args) {
        int[] prices = {1,2,3,0,2};
        System.out.println(new Solution().maxProfit(prices));
    }
}

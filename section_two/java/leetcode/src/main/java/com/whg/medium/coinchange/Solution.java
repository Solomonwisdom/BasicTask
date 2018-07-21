package com.whg.medium.coinchange;

/**
 * Solution class
 *
 * problemId 322
 * @author wanghaogang
 * @date 2018/6/20
 */
public class Solution {

    private int min(int a,int b) {
        return a<b?a:b;
    }

    public int coinChange(int[] coins, int amount) {
        if(coins==null) {
            return -1;
        }
        int[] dp = new int[amount+1];
        dp[0]=0;
        for (int i=1; i<=amount; ++i) {
            dp[i]=amount+1;
            for (int j=0; j<coins.length; ++j) {
                if(coins[j]<=i) {
                    dp[i]=min(dp[i], dp[i-coins[j]]+1);
                }
            }
        }
        return dp[amount]<=amount?dp[amount]:-1;
    }

    public static void main(String[] args) {
        int[] coins = new int[]{186,419,83,408};
        System.out.println(new Solution().coinChange(coins,6249));
    }
}

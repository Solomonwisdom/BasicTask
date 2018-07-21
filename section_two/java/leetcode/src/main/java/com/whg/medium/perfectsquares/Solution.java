package com.whg.medium.perfectsquares;

/**
 * Solution class
 *
 * problemId 279
 * @author wanghaogang
 * @date 2018/6/26
 */
public class Solution {

    private int min(int a,int b) {
        return a<b?a:b;
    }
    public int numSquares(int n) {
        if (n==0) {
            return 0;
        }
        int[] dp = new int[n+1];
        for (int i=1;i*i<=n;++i) {
            dp[i*i]=1;
        }
        if(dp[n]==1) {
            return 1;
        }
        for (int i=2;i<=n;++i) {
            if(dp[i]==0) {
                dp[i]=i;
                for (int j=1;j*j<=i;++j) {
                    dp[i]=min(dp[i],dp[i-j*j]+1);
                }
            }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        int[] nums = {12,13};
        for (int num: nums) {
            System.out.println(new Solution().numSquares(num));
        }
    }
}

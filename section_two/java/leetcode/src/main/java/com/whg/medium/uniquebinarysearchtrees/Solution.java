package com.whg.medium.uniquebinarysearchtrees;


/**
 * Solution class
 *
 * problemId 96
 * @author wanghaogang
 * @date 2018/6/27
 */
public class Solution {

    public int numTrees(int n) {
        int[] dp = new int[n + 1];
        dp[0] = dp[1] = 1;
        for (int i = 2; i <= n; ++i) {
            for (int j = 0; j < i; ++j) {
                dp[i] += dp[j] * dp[i - j - 1];
            }
        }
        return dp[n];
    }

    public static void main(String[] args) {
        System.out.println(new Solution().numTrees(3));
    }
}

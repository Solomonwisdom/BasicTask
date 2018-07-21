package com.whg.hard.editdistance;

/**
 * Solution class
 *
 * problemId 72
 * @author wanghaogang
 * @date 2018/6/18
 */
public class Solution {
    private int min(int a,int b) {
        return a<b?a:b;
    }
    public int minDistance(String word1, String word2) {
        final int inf = 0x3f3f3f3f;
        int n=word1.length(), m=word2.length();
        int[][] dp = new int[n+1][m+1];
        dp[0][0]=0;
        for(int j=1; j<=m; ++j) {
            dp[0][j]=j;
        }
        for(int i=1; i<=n; ++i) {
            dp[i][0]=i;
            for(int j=1; j<=m; ++j) {
                if(word1.charAt(i-1)==word2.charAt(j-1)) {
                    dp[i][j]=dp[i-1][j-1];
                } else {
                    // 替换
                    dp[i][j]=dp[i-1][j-1]+1;
                }
                // 删掉
                dp[i][j]=min(dp[i][j], dp[i-1][j]+1);
                // 插入
                dp[i][j]=min(dp[i][j], dp[i][j-1]+1);
            }
        }
        return dp[n][m];
    }
    public static void main(String[] args) {
        String word1="intention", word2="execution";
        System.out.println(new Solution().minDistance(word1,word2));
    }
}

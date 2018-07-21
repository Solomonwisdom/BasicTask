package com.whg.hard.longestvalidparentheses;
// problemId:32

public class Solution {
    public int longestValidParentheses(String s) {
        int ans=0;
        int[] dp=new int[s.length()+1];
        for (int i=1; i<=s.length(); ++i) {
            if(i>=2&&s.charAt(i-1)==')') {
                if(s.charAt(i-2)=='(') {
                    dp[i]=dp[i-2]+2;
                } else if(i-dp[i-1]>1&&s.charAt(i-dp[i-1]-2)=='('){
                    dp[i]=dp[i-1]+2+dp[i-dp[i-1]-2];
                }
            }
            if(ans<dp[i]) {
                ans=dp[i];
            }
        }
        return ans;
    }
    public static void main(String[] args) {
        String s[] = {"()(()","()())","(()()","(()"};
        for (String s1:s) {
            System.out.println("" + new Solution().longestValidParentheses(s1));
        }
    }
}

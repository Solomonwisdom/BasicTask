package com.whg.hard.regularexpressionmatching;

/**
 * Solution class
 *
 * problemId 10
 * @author wanghaogang
 * @date 2018/6/16
 */
public class Solution {
    private int[][] mem;
    private char x = '*';
    public boolean isMatch(String s, String p) {
        mem = new int[s.length()+1][p.length()+1];
        for (int i=0;i<=s.length();++i) {
            for (int j=0;j<=p.length();++j) {
                mem[i][j]=-1;
            }
        }
        return dp(0,0,s,p);
    }
    public boolean dp(int i,int j,String s,String p) {
        if(mem[i][j]!=-1) {
            return mem[i][j]>0;
        }
        boolean ans;
        if(j==p.length()) {
            ans = i==s.length();
        } else {
            boolean firstMatch = (i < s.length() &&
                    (p.charAt(j) == s.charAt(i) ||
                            p.charAt(j) == '.'));

            if (j + 1 < p.length() && p.charAt(j+1) == x){
                ans = dp(i, j+2, s, p) || (firstMatch && dp(i+1, j, s, p));
            } else {
                ans = firstMatch && dp(i+1, j+1, s, p);
            }
        }
        mem[i][j]=ans?1:0;
        return ans;
    }
}

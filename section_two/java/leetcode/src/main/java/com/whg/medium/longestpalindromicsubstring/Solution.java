package com.whg.medium.longestpalindromicsubstring;


import java.util.Arrays;

/**
 * Solution class
 *
 * problemId 5
 * @author wanghaogang
 * @date 2018/6/20
 */
public class Solution {

    private int min(int a,int b) {
        return a<b?a:b;
    }
    public String longestPalindrome(String s) {
        if(s==null||s.length()==0) {
            return "";
        }
        // 马拉车算法，p[i]记录以i为中心，最长向左或右衍生的回文串半长度。
        int[] p = new int[s.length()*2+5];
        char[] c = new char[s.length()*2+5];
        int ans=1;
        int len=0;
        for (int i=0;i<s.length();++i) {
            c[++len]='%';
            c[++len]=s.charAt(i);
        }
        c[len+1]='%';
        // R是回文子串能覆盖到的最右位置,pos为对应的中心
        int pos=0,r=0;
        int ansL=2,ansR=2;
        p[0]=1;
        for(int i=1;i<len;++i) {
            p[i]=i<r?min(p[2*pos-i],r-i):1;
            while (1<=i-p[i]&&c[i-p[i]]==c[i+p[i]]) {
                ++p[i];
            }
            if(i+p[i]>r) {
                pos=i;
                r=i+p[i];
            }
            if(p[i]-1>ans) {
                // 因为插入了'%'的缘故，p[i]对应的实际回文串长度为p[i]-1
                ansL = i-p[i]+1;
                ansR = i+p[i]-1;
                ans=p[i]-1;
            }
        }
        StringBuilder ansStr = new StringBuilder();
        for (int i=ansL;i<=ansR;++i) {
            if(c[i]!='%') {
                ansStr.append(c[i]);
            }
        }
        return ansStr.toString();
    }

    public static void main(String[] args) {
        String[] strings = {"a","abcabcbb", "bbbbb", "pwwkew", "au","babad","cbbd"};
        for (String s : strings) {
            System.out.println(new Solution().longestPalindrome(s));
        }
    }
}

package com.whg.hard.minimumwindowsubstring;

import java.util.Arrays;
/**
 * Solution class
 *
 * problemId 76
 * @author wanghaogang
 * @date 2018/6/16
 */

public class Solution {
    public String minWindow(String s, String t) {
        if(t.length()==0) {
            return "";
        }
        int l=0,r=0,cnt=0;
        int[] tCnt=new int[300];
        int[] sCnt=new int[300];
        Arrays.fill(tCnt,0);
        Arrays.fill(sCnt,0);
        for (int i=0;i<t.length();++i) {
            ++tCnt[t.charAt(i)];
        }
        int ansL=-1,ansR=-1,ansLen=s.length()+5;
        while(r<s.length()) {
            ++sCnt[s.charAt(r)];
            if(sCnt[s.charAt(r)]<=tCnt[s.charAt(r)]) {
                ++cnt;
                while(cnt==t.length()) {
                    if(r-l+1<ansLen) {
                        ansLen = r - l + 1;
                        ansL = l;
                        ansR = r;
                    }
                    --sCnt[s.charAt(l)];
                    if(sCnt[s.charAt(l)]<tCnt[s.charAt(l)]) {
                        --cnt;
                    }
                    ++l;
                }
            }
            ++r;
        }
        if(ansL==-1) {
            return "";
        } else {
            return s.substring(ansL,ansR+1);
        }
    }
    public static void main(String[] args) {
//        String S = "ADOBECODEBANC", T = "ABC";
        String S="a",T="a";
        System.out.println(new Solution().minWindow(S,T));
    }
}

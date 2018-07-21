package com.whg.easy.findallanagramsinastring;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution class
 *
 * problemId 438
 * @author wanghaogang
 * @date 2018/6/29
 */

class Solution {

    public List<Integer> findAnagrams(String s, String p) {
        if (s==null||p==null||s.length()<p.length()) {
            return new ArrayList<>();
        }
        List<Integer> ans = new ArrayList<>();
        int[] totalCount = new int[26], curCount = new int[26];
        for (char ch: p.toCharArray()) {
            ++totalCount[ch-'a'];
        }
        int count=0;
        for (int i=0;i<p.length();++i) {
            int idx=s.charAt(i)-'a';
            if(++curCount[idx]<=totalCount[idx]) {
                ++count;
            }
        }
        if (count==p.length()) {
            ans.add(0);
        }
        for (int i=p.length();i<s.length();++i) {
            int idx=s.charAt(i-p.length())-'a';
            if (--curCount[idx]<totalCount[idx]) {
                --count;
            }
            idx=s.charAt(i)-'a';
            if(++curCount[idx]<=totalCount[idx]) {
                ++count;
            }
            if (count>=p.length()) {
                ans.add(i-p.length()+1);
            }
        }
        return ans;
    }
}

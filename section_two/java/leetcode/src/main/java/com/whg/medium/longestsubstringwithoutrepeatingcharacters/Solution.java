package com.whg.medium.longestsubstringwithoutrepeatingcharacters;


import java.util.Arrays;

/**
 * Solution class
 *
 * problemId 3
 * @author wanghaogang
 * @date 2018/6/20
 */
public class Solution {

    public int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        boolean[] vis = new boolean[300];
        Arrays.fill(vis, false);
        int ans = 1;
        int l = 0, r = 0;
        for (; r < s.length(); ++r) {
            if (vis[s.charAt(r)]) {
                if (ans < r - l) {
                    ans = r - l;
                }
                while (vis[s.charAt(r)]) {
                    vis[s.charAt(l)] = false;
                    l++;
                }

            }
            vis[s.charAt(r)] = true;
        }
        if (ans < r - l) {
            ans = r - l;
        }
        return ans;
    }

    public static void main(String[] args) {
        String[] strings = {"abcabcbb", "bbbbb", "pwwkew", "au"};
        for (String s : strings) {
            System.out.println(new Solution().lengthOfLongestSubstring(s));
        }
    }
}

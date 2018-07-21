package com.whg.medium.palindromicsubstrings;

/**
 * Solution class
 *
 * problemId 647
 * @author wanghaogang
 * @date 2018/6/28
 */
public class Solution {

    public int countSubstrings(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        // 马拉车算法，p[i]记录以i为中心，最长向左或右衍生的回文串半长度。
        int[] p = new int[s.length() * 2 + 3];
        char[] c = new char[p.length];
        c[0] = '@';
        int len = 0;
        for (int i = 0; i < s.length(); ++i) {
            c[++len] = '%';
            c[++len] = s.charAt(i);
        }
        c[++len] = '%';
        c[++len] = '$';
        // R是回文子串能覆盖到的最右位置,pos为对应的中心
        int pos = 0, r = 0;
        int ans = 0;
        p[0] = 0;
        for (int i = 1; i < p.length - 1; ++i) {
            p[i] = i < r ? Math.min(p[2 * pos - i], r - i) : 0;
            while (c[i - p[i] - 1] == c[i + p[i] + 1]) {
                ++p[i];
            }
            if (i + p[i] > r) {
                pos = i;
                r = i + p[i];
            }
            ans += (p[i] + 1) / 2;
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(new Solution().countSubstrings("abc"));
    }

}

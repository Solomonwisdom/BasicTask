package com.whg.hard.removeinvalidparentheses;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution class
 *
 * problemId 315
 * @author wanghaogang
 * @date 2018/6/19
 */
public class Solution {
    public List<String> removeInvalidParentheses(String s) {
        List<String> ans = new ArrayList<>();
        process(s, ans, 0, 0, new char[]{'(', ')'});
        return ans;
    }

    private void process(String s, List<String> ans, int lastI, int lastJ, char[] par) {
        int stk = 0;
        for (int i = lastI; i < s.length(); ++i) {
            if (s.charAt(i) == par[0]) {
                ++stk;
            } else if (s.charAt(i) == par[1]) {
                --stk;
            }
            if (stk < 0) {
                for (int j = lastJ; j <= i; ++j) {
                    if (s.charAt(j) == par[1]) {
                        if (j == lastJ || s.charAt(j - 1) != par[1]) {
                            process(s.substring(0, j) + s.substring(j + 1, s.length()), ans, i, j, par);
                        }
                    }
                }
                return;
            }
        }
        String rev = new StringBuilder(s).reverse().toString();
        char par0 = '(';
        if (par[0] == par0) {
            process(rev, ans, 0, 0, new char[]{')', '('});
        } else {
            ans.add(rev);
        }
    }

    public static void main(String[] args) {
        String s = ")(";
        for (String x : new Solution().removeInvalidParentheses(s)) {
            System.out.println(x);
        }
    }
}

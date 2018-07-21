package com.whg.medium.wordbreak;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Solution class
 *
 * problemId 139
 * @author wanghaogang
 * @date 2018/6/25
 */
public class Solution {

    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] dp = new boolean[s.length() + 1];
        dp[0] = true;
        for (int i = 1; i <= s.length(); ++i) {
            for (String word : wordDict) {
                if (word.length() <= i && dp[i - word.length()]) {
                    boolean flag = true;
                    for (int j = 0; j < word.length(); ++j) {
                        if (s.charAt(i - word.length() + j) != word.charAt(j)) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        dp[i] = true;
                        break;
                    }
                }
            }
        }
        return dp[s.length()];
    }

    public static void main(String[] args) {
        String s = "catsandog";
        String[] a = {"cats", "dog", "sand", "and", "cat"};
        List<String> wordDict = Arrays.asList(a);
        System.out.println(new Solution().wordBreak(s,wordDict));
    }
}

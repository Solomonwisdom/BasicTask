package com.whg.medium.generateparentheses;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution class
 *
 * problemId 22
 * @author wanghaogang
 * @date 2018/6/28
 */
public class Solution {

    private void dfs(int leftNum, int rightNum, int n, StringBuilder cur, List<String> ans) {
        if (leftNum == n && rightNum == n) {
            ans.add(cur.toString());
            return;
        }
        if (leftNum < n) {
            cur.append('(');
            dfs(leftNum + 1, rightNum, n, cur, ans);
            cur.deleteCharAt(cur.length() - 1);
        }
        if (leftNum > rightNum) {
            cur.append(')');
            dfs(leftNum, rightNum + 1, n, cur, ans);
            cur.deleteCharAt(cur.length() - 1);
        }
    }

    public List<String> generateParenthesis(int n) {
        List<String> ans = new ArrayList<>();
        dfs(0, 0, n, new StringBuilder(), ans);
        return ans;
    }

}

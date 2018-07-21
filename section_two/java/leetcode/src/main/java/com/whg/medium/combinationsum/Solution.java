package com.whg.medium.combinationsum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Solution class
 *
 * problemId 39
 * @author wanghaogang
 * @date 2018/6/27
 */
public class Solution {

    private void dfs(int low, int target, List<Integer> cur, int[] candidates, List<List<Integer>> ans) {
        if (target == 0) {
            ans.add(new ArrayList<>(cur));
            return;
        }
        for (int i = low; i < candidates.length; ++i) {
            int canditate = candidates[i];
            if (target < canditate) {
                break;
            }
            cur.add(canditate);
            dfs(i, target - canditate, cur, candidates, ans);
            cur.remove(cur.size() - 1);
        }
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        if (candidates == null || candidates.length == 0) {
            return new ArrayList<>();
        }
        Arrays.sort(candidates);
        List<List<Integer>> ans = new ArrayList<>();
        dfs(0, target, new ArrayList<>(), candidates, ans);
        return ans;
    }

    public static void main(String[] args) {
        int[] candidates = {2,3,6,7};
        int target = 7;
        new Solution().combinationSum(candidates,target);
    }
}

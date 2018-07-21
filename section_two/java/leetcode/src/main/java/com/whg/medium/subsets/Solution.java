package com.whg.medium.subsets;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution class
 *
 * problemId 78
 * @author wanghaogang
 * @date 2018/6/28
 */
public class Solution {

    private void dfs(int[] nums, int pos, List<Integer> curList, List<List<Integer>> ans) {
        if (pos == nums.length) {
            ans.add(new ArrayList<>(curList));
            return;
        }
        dfs(nums, pos + 1, curList, ans);
        curList.add(nums[pos]);
        dfs(nums, pos + 1, curList, ans);
        curList.remove(curList.size() - 1);
    }

    public List<List<Integer>> subsets(int[] nums) {
        List<Integer> curList = new ArrayList<>();
        List<List<Integer>> ans = new ArrayList<>();
        dfs(nums, 0, curList, ans);
        return ans;
    }

}

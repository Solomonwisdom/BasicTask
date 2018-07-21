package com.whg.medium.permutations;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution class
 *
 * problemId 46
 * @author wanghaogang
 * @date 2018/6/28
 */
public class Solution {

    private void swap(int[] nums, int x, int y) {
        int tmp = nums[x];
        nums[x] = nums[y];
        nums[y] = tmp;
    }

    private void dfs(int[] nums, int pos, List<Integer> curList, List<List<Integer>> ans) {
        if (nums.length == pos) {
            ans.add(new ArrayList<>(curList));
            return;
        }
        curList.add(nums[pos]);
        dfs(nums, pos + 1, curList, ans);
        curList.remove(curList.size() - 1);
        for (int j = pos + 1; j < nums.length; ++j) {
            swap(nums, pos, j);
            curList.add(nums[pos]);
            dfs(nums, pos + 1, curList, ans);
            swap(nums, pos, j);
            curList.remove(curList.size() - 1);
        }
    }

    public List<List<Integer>> permute(int[] nums) {
        List<Integer> curList = new ArrayList<>();
        List<List<Integer>> ans = new ArrayList<>();
        dfs(nums, 0, curList, ans);
        return ans;
    }

}

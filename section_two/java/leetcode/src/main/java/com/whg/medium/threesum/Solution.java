package com.whg.medium.threesum;

import java.util.*;

/**
 * Solution class
 *
 * problemId 15
 * @author wanghaogang
 * @date 2018/6/19
 */
public class Solution {

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<List<Integer>>();
        int three = 3;
        if (nums == null || nums.length < three) {
            return ans;
        }
        Set<Integer> set = new HashSet<Integer>();
        Set<List<Integer>> ansSet = new HashSet<>();
        Arrays.sort(nums);
        set.add(nums[0]);
        for (int i = 1; i < nums.length - 1; ++i) {
            if (i > 1 && nums[i] == nums[i - 1]) {
                if (i < nums.length - 2 && nums[i] == nums[i + 1]) {
                    if (nums[i]==0) {
                        List<Integer> tmp = new ArrayList<>();
                        tmp.add(0);
                        tmp.add(0);
                        tmp.add(0);
                        ansSet.add(tmp);
                    }
                    continue;
                }
            }
            for (int j = i + 1; j < nums.length; ++j) {
                if (j > i + 1 && nums[j] == nums[j - 1]) {
                    continue;
                }
                if (set.contains(-nums[i] - nums[j])) {
                    List<Integer> tmp = new ArrayList<>();
                    tmp.add(-nums[i] - nums[j]);
                    tmp.add(nums[i]);
                    tmp.add(nums[j]);
                    ansSet.add(tmp);
                }
            }
            set.add(nums[i]);
        }
        ans.addAll(ansSet);
        return ans;
    }

    public static void main(String[] args) {
//        int[] nums = {-1, 0, 1, 2, -1, -4};
//        int[] nums = {-2, 0, 1, 1, 2};
//        int[] nums = {0, 0, 0};
        int[] nums = {-4,-2,1,-5,-4,-4,4,-2,0,4,0,-2,3,1,-5,0};
        new Solution().threeSum(nums);
    }
}

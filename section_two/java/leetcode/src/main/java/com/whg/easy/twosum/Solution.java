package com.whg.easy.twosum;

import java.util.HashMap;
import java.util.Map;

/**
 * Solution class
 *
 * problemId 1
 * @author wanghaogang
 * @date 2018/6/29
 */

class Solution {

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; ++i) {
            int tmp = target - nums[i];
            if (map.containsKey(tmp)) {
                return new int[]{map.get(tmp), i};
            }
            map.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

}

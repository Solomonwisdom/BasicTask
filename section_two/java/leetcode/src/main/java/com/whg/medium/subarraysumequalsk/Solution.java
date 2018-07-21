package com.whg.medium.subarraysumequalsk;

import java.util.HashMap;
import java.util.Map;

/**
 * Solution class
 *
 * problemId 560
 * @author wanghaogang
 * @date 2018/6/26
 */
public class Solution {

    public int subarraySum(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        Map<Integer, Integer> map = new HashMap<>(nums.length);
        int sum = 0, ans = 0;
        map.put(0, 1);
        for (int num : nums) {
            sum += num;
            if (map.containsKey(sum - k)) {
                ans += map.get(sum - k);
            }
            if (map.containsKey(sum)) {
                map.put(sum, map.get(sum) + 1);
            } else {
                map.put(sum, 1);
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] nums = {1, 1, 1, 1, 1, 1, 1, 1, 1};
        int k = 3;
        System.out.println(new Solution().subarraySum(nums, k));
    }
}

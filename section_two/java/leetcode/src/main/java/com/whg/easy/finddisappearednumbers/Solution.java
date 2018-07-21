package com.whg.easy.finddisappearednumbers;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution class
 *
 * problemId 448
 * @author wanghaogang
 * @date 2018/6/29
 */

class Solution {

    public List<Integer> findDisappearedNumbers(int[] nums) {
        List<Integer> ans = new ArrayList<>();
        for (int x : nums) {
            int absX = Math.abs(x);
            if (nums[absX - 1] > 0) {
                nums[absX - 1] = -nums[absX - 1];
            }
        }
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] > 0) {
                ans.add(i + 1);
            }
        }
        return ans;
    }
}

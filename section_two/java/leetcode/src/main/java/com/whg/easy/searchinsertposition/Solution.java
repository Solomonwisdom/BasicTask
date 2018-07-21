package com.whg.easy.searchinsertposition;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution class
 *
 * problemId 35
 * @author wanghaogang
 * @date 2018/6/29
 */

class Solution {

    public int searchInsert(int[] nums, int target) {
        if (target > nums[nums.length - 1]) {
            return nums.length;
        }
        if (target < nums[0]) {
            return 0;
        }
        int l = 0, r = nums.length - 1, ans = -1;
        while (l <= r) {
            int mid = (l + r) / 2;
            if (nums[mid] == target) {
                ans = mid;
                break;
            }
            if (nums[mid] < target) {
                l = mid + 1;
            } else {
                r = mid - 1;
                ans = mid;
            }
        }
        return ans;
    }
}

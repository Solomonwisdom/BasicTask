package com.whg.easy.climbingstairs;

/**
 * Solution class
 *
 * problemId 70
 * @author wanghaogang
 * @date 2018/6/29
 */

class Solution {

    public int climbStairs(int n) {
        if (n == 0) {
            return 1;
        }
        final int two = 2;
        if (n <= two) {
            return n;
        }
        int pre2 = 1, pre1 = 2, ans = 0;
        for (int i = 3; i <= n; ++i) {
            ans = pre1 + pre2;
            pre2 = pre1;
            pre1 = ans;
        }
        return ans;
    }
}

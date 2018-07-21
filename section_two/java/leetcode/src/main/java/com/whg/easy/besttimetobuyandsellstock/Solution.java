package com.whg.easy.besttimetobuyandsellstock;

/**
 * Solution class
 *
 * problemId 121
 * @author wanghaogang
 * @date 2018/6/29
 */

class Solution {

    public int maxProfit(int[] prices) {
        if (prices == null || prices.length <= 1) {
            return 0;
        }
        int min = prices[0], ans = 0;
        for (int i = 1; i < prices.length; ++i) {
            ans = Math.max(prices[i] - min, ans);
            min = Math.min(min, prices[i]);
        }
        return ans;
    }

}

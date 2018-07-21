package com.whg.medium.countingbits;

/**
 * Solution class
 *
 * problemId 338
 * @author wanghaogang
 * @date 2018/6/28
 */
public class Solution {

    public int[] countBits(int num) {
        int[] ans = new int[num+1];
        for (int i=1;i<=num;++i) {
            ans[i]=ans[i-(i&(-i))]+1;
        }
        return ans;
    }

}

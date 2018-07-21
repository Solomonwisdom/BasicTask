package com.whg.hard.burstballoons;

import java.util.HashSet;
import java.util.Set;

/**
 * Solution class
 *
 * problemId 312
 * @author wanghaogang
 * @date 2018/6/19
 */
public class Solution {

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    public int maxCoins(int[] nums) {
        if(nums==null||nums.length==0) {
            return 0;
        }
        int[][] dp = new int[nums.length][nums.length];
        for(int len=1; len<=nums.length; ++len) {
            for (int l=0; l+len-1<nums.length; ++l) {
                int r=l+len-1;
                // 枚举最后一个消除的数
                for (int j=l;j<=r;++j) {
                    int tmp=(l-1<0?1:nums[l-1])*nums[j]*(r+1>=nums.length?1:nums[r+1]);
                    if (j>l) {
                        tmp+=dp[l][j-1];
                    }
                    if(j<r) {
                        tmp+=dp[j+1][r];
                    }
                    dp[l][r]=max(tmp,dp[l][r]);
                }
            }
        }
        return dp[0][nums.length-1];
    }

    public static void main(String[] args) {
        int[] nums={3,1,5,8};
        System.out.println(new Solution().maxCoins(nums));
    }
}

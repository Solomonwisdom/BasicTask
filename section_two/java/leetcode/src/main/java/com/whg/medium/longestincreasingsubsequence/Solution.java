package com.whg.medium.longestincreasingsubsequence;

import java.util.Arrays;

/**
 * Solution class
 *
 * problemId 300
 * @author wanghaogang
 * @date 2018/6/26
 */
public class Solution {

    private int lowerBound(int[] g,int x,int l,int r) {
        int ans = -1;
        while(l<=r) {
            int mid=(l+r)/2;
            if(g[mid]<x) {
                l=mid+1;
            } else {
                ans = mid;
                r = mid-1;
            }
        }
        return ans;
    }

    public int lengthOfLIS(int[] nums) {
        if(nums==null||nums.length==0) {
            return 0;
        }
        int[] g = new int[nums.length+1];
        int ans=1,inf = 0x7fffffff;
        Arrays.fill(g, inf);
        for (int i=0;i<nums.length;++i) {
            int k = lowerBound(g,nums[i],1,nums.length);
            if(k>ans) {
                ans = k;
            }
            g[k]=nums[i];
        }
        return ans;
    }

    public static void main(String[] args) {
        int[][] numss = {{1, 5, 11, 12},{1, 2, 3, 2},{10,9,2,5,3,7,101,18}};
        for (int[] nums: numss) {
            System.out.println(new Solution().lengthOfLIS(nums));
        }
    }
}


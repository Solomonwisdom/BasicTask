package com.whg.hard.trappingrainwater;

/**
 * Solution class
 *
 * problemId 42
 * @author wanghaogang
 * @date 2018/6/19
 */
public class Solution {
    private int min(int a, int b) {
        return a < b ? a : b;
    }

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    public int trap(int[] height) {
        if(height==null||height.length==0) {
            return 0;
        }
        int[] left=new int[height.length];
        int[] right=new int[height.length];
        left[0]=height[0];
        right[height.length-1]=height[height.length-1];
        for (int i=1;i<height.length;++i) {
            left[i]=max(left[i-1],height[i]);
        }
        for (int i=height.length-2;i>=0;--i) {
            right[i]=max(right[i+1],height[i]);
        }
        int ans=0;
        for (int i=0;i<height.length;++i) {
            ans+=min(left[i],right[i])-height[i];
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] heights={0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println(new Solution().trap(heights));
    }
}

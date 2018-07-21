package com.whg.medium.containerwithmostwater;

/**
 * Solution class
 *
 * problemId 11
 * @author wanghaogang
 * @date 2018/6/26
 */
public class Solution {

    private int max(int a,int b) {
        return a>b?a:b;
    }
    private int min(int a,int b) {
        return a<b?a:b;
    }
    public int maxArea(int[] height) {
        int ans = 0,l=0,r=height.length-1;
        while (l<r) {
            ans = max(ans, min(height[l],height[r])*(r-l));
            if(height[l]<height[r]) {
                ++l;
            } else {
                --r;
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] height = {1,2,3,4,5};
        System.out.println(new Solution().maxArea(height));
    }
}

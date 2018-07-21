package com.whg.hard.largestrectangleinhistogram;
/**
 * Solution class
 *
 * problemId 84
 * @author wanghaogang
 * @date 2018/6/17
 */

public class Solution {
    private int max(int a,int b) {
        return a>b?a:b;
    }
    public int largestRectangleArea(int[] heights) {
        int[] stack = new int[heights.length+1];
        int top=0;
        int ans=0;
        for (int i=0; i<=heights.length; ++i) {
            int h=(i<heights.length?heights[i]:0);
            if(top==0||heights[stack[top-1]]<=h) {
                stack[top++]=i;
            } else {
                int tmp=stack[--top];
                ans = max(ans,heights[tmp]*(top==0?i:i-1-stack[top-1]));
                --i;
            }
        }
        return ans;
    }
    public static void main(String[] args) {
        int[] a = {2, 1, 5, 6, 2, 3};
        int[] heights = {6, 2, 5, 4, 5, 1, 6};
        System.out.println(new Solution().largestRectangleArea(heights));
    }
}

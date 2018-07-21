package com.whg.medium.searchforarange;

/**
 * Solution class
 *
 * problemId 34
 * @author wanghaogang
 * @date 2018/6/25
 */
public class Solution {

    public int[] searchRange(int[] nums, int target) {
        int a=-1,b=-1,l=0,r=nums.length-1;
        while(l<=r) {
            int mid = l+(r-l)/2;
            if(nums[mid]<target) {
                l=mid+1;
            } else  {
                if(nums[mid]==target) {
                    a=mid;
                }
                r = mid-1;
            }
        }
        l = 0;
        r = nums.length-1;
        while(l<=r) {
            int mid = l+(r-l)/2;
            if(nums[mid]>target) {
                r=mid-1;
            } else  {
                if(nums[mid]==target) {
                    b=mid;
                }
                l = mid+1;
            }
        }
        return new int[]{a,b};
    }
    public static void main(String[] args) {
        int[] nums={5,7,7,8,8,10};
        int target = 5;
        int[] ans = new Solution().searchRange(nums, target);
        for (int x:ans) {
            System.out.print(x+",");
        }
        System.out.println();
    }
}

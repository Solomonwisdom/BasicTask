package com.whg.medium.maximumproductsubarray;

/**
 * Solution class
 *
 * problemId 152
 * @author wanghaogang
 * @date 2018/6/20
 */
public class Solution {

    public int maxProduct(int[] nums) {
        if(nums==null||nums.length==0) {
            return 0;
        }
        if(nums.length==1) {
            return nums[0];
        }
        int[] positiveDp = new int[nums.length];
        int[] negativeDp = new int[nums.length];
        if(nums[0]>0) {
            positiveDp[0]=nums[0];
        } else {
            negativeDp[0]=nums[0];
        }
        int ans=positiveDp[0];
        for (int i=1;i<nums.length;++i) {
            if(nums[i]>0) {
                negativeDp[i]=(negativeDp[i-1]<0?negativeDp[i-1]:1)*nums[i];
                positiveDp[i]=(positiveDp[i-1]<=0?1:positiveDp[i-1])*nums[i];
            } else {
                negativeDp[i]=(positiveDp[i-1]>0?positiveDp[i-1]:1)*nums[i];
                positiveDp[i]=(negativeDp[i-1]>=0?1:negativeDp[i-1])*nums[i];
            }
            if(positiveDp[i]>ans) {
                ans=positiveDp[i];
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] nums = new int[]{2,-1,1,1};
        System.out.println(new Solution().maxProduct(nums));
    }
}

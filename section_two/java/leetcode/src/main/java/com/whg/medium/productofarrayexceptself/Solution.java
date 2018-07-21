package com.whg.medium.productofarrayexceptself;

import java.util.Arrays;

/**
 * Solution class
 *
 * problemId 238
 * @author wanghaogang
 * @date 2018/6/28
 */
public class Solution {

    public int[] productExceptSelf(int[] nums) {
        if (nums==null||nums.length==0) {
            return null;
        }
        if (nums.length==1) {
            return new int[]{1};
        }
        int[] ans = new int[nums.length];
        Arrays.fill(ans,1);
        int leftProduct=1,rightProduct=1;
        for (int i=0;i<nums.length;++i) {
            ans[i]*=leftProduct;
            leftProduct*=nums[i];
        }
        for (int j=nums.length-1;j>=0;--j) {
            ans[j]*=rightProduct;
            rightProduct*=nums[j];
        }
        return ans;
    }

}

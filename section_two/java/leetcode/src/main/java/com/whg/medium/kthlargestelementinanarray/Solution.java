package com.whg.medium.kthlargestelementinanarray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Solution class
 *
 * problemId 215
 * @author wanghaogang
 * @date 2018/6/26
 */
public class Solution {

    private int partition(int[] nums, int start, int end) {
        int pIndex = start;
        int pivot = nums[end];
        for (int i=start;i<end;++i) {
            if (nums[i]>pivot) {
                int tmp=nums[i];
                nums[i]=nums[pIndex];
                nums[pIndex]=tmp;
                ++pIndex;
            }
        }
        int tmp = nums[pIndex];
        nums[pIndex] = nums[end];
        nums[end] = tmp;
        return pIndex;
    }

    public int findKthLargest(int[] nums, int k) {
        --k;
        int l = 0, r = nums.length-1;
        while (l<=r) {
            int m = partition(nums,l,r);
            if (m==k) {
                return nums[k];
            } else if (m<k) {
                l=m+1;
            } else {
                r = m-1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int[][] numss = {{3,2,1,5,6,4},{3,2,3,1,2,4,5,5,6}};
        int[] ks = {6,8};
        for (int i=0;i<ks.length;++i) {
            System.out.println(new Solution().findKthLargest(numss[i],ks[i]));
        }
    }
}

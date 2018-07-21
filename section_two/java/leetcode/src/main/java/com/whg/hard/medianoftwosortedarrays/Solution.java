package com.whg.hard.medianoftwosortedarrays;
/**
 * Solution class
 *
 * problemId 4
 * @author wanghaogang
 * @date 2018/6/15
 */

public class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int len=nums1.length+nums2.length,two = 2;;
        int i=0,j=0,k=0;
        int a=(len+1)/two,b=-1;
        if(len%two==0) {
            b=len/two+1;
        }
        double ans=0;
        while(i<nums1.length&&j<nums2.length) {
            ++k;
            if(k>a&&k>b) {
                break;
            }
            if(nums1[i]<nums2[j]) {
                if(k==a||k==b) {
                    ans+=nums1[i];
                }
                ++i;
            } else {
                if(k==a||k==b) {
                    ans+=nums2[j];
                }
                ++j;
            }
        }
        if(k>a&&k>b) {
            if(b!=-1) {
                ans/=2;
            }
            return ans;
        }
        while (i<nums1.length) {
            ++k;
            if(k>a&&k>b) break;
            if(k==a||k==b) ans+=nums1[i];
            ++i;
        }
        while (j<nums2.length) {
            ++k;
            if(k>a&&k>b) break;
            if(k==a||k==b) ans+=nums2[j];
            ++j;
        }
        if(b!=-1) {
            ans/=two;
        }
        return ans;
    }
}

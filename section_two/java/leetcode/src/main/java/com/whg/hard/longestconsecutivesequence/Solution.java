package com.whg.hard.longestconsecutivesequence;

import java.util.HashSet;
import java.util.Set;

/**
 * Solution class
 *
 * problemId 128
 * @author wanghaogang
 * @date 2018/6/19
 */
public class Solution {

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    public int longestConsecutive(int[] nums) {
        if(nums==null||nums.length==0) {
            return 0;
        }
        Set<Integer> set = new HashSet<>();
        for (int x:nums) {
            set.add(x);
        }
        int ans=0,cur;
        for (int x:nums) {
            if(!set.contains(x-1)) {
                cur=1;
                for (int i=1; set.contains(x+i); ++i) {
                    ++cur;
                }
                ans =max(ans,cur);
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[] nums={100, 4, 200, 1, 3, 2};
        System.out.println(new Solution().longestConsecutive(nums));
    }
}

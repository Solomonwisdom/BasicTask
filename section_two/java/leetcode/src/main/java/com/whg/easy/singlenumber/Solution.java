package com.whg.easy.singlenumber;

import java.util.HashSet;
import java.util.Set;

/**
 * Solution class
 *
 * problemId 136
 * @author wanghaogang
 * @date 2018/6/29
 */

class Solution {

    public int singleNumber(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (set.contains(num)) {
                set.remove(num);
            } else {
                set.add(num);
            }
        }
        return (Integer) set.toArray()[0];
    }
}

package com.whg.easy.hammingdistance;

/**
 * Solution class
 *
 * problemId 461
 * @author wanghaogang
 * @date 2018/6/29
 */

class Solution {

    public int hammingDistance(int x, int y) {
        x=x^y;
        int count=0;
        while (x!=0) {
            count++;
            x-=(x&(-x));
        }
        return count;
    }
}

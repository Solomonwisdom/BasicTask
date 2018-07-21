package com.whg.medium.uniquepaths;

import java.math.BigInteger;

/**
 * Solution class
 *
 * problemId 62
 * @author wanghaogang
 * @date 2018/6/28
 */
public class Solution {

    private long binCoeff(int n,int k) {
        if (k==0) {
            return 1;
        }
        if(k>n-k) {
            k=n-k;
        }
        long ans = 1;
        for (int i=0;i<k;++i) {
            ans = ans*(n-i)/(i+1);
        }
        return ans;
    }

    public int uniquePaths(int m, int n) {
        if (m == 0 || n == 0) {
            return 0;
        }
        --m;
        --n;
        return (int)binCoeff(m+n,m);
    }


    public static void main(String[] args) {
        System.out.println(new Solution().uniquePaths(100, 100));
    }
}

package com.whg.medium.taskscheduler;

import java.util.Arrays;

/**
 * Solution class
 *
 * problemId 621
 * @author wanghaogang
 * @date 2018/6/27
 */
public class Solution {

    private int max(int a, int b) {
        return a > b ? a : b;
    }

    public int leastInterval(char[] tasks, int n) {
        int[] c = new int[26];
        for (char t : tasks) {
            c[t - 'A']++;
        }
        Arrays.sort(c);
        int i = 25;
        while (i >= 0 && c[i] == c[c.length - 1]) {
            --i;
        }
        return max(tasks.length, (c[25] - 1) * (n + 1) + 25 - i);
    }

    public static void main(String[] args) {
        char[] tasks = {'A', 'A', 'A', 'B', 'B', 'B'};
        int n = 2;
        System.out.println(new Solution().leastInterval(tasks, n));
    }
}

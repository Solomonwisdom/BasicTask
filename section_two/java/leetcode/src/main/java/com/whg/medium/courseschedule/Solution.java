package com.whg.medium.courseschedule;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Solution class
 *
 * problemId 207
 * @author wanghaogang
 * @date 2018/6/25
 */
public class Solution {

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> g = new ArrayList<>(numCourses);
        for (int i=0;i<numCourses;++i) {
            g.add(new ArrayList<>());
        }
        int cnt = 0;
        int[] inDegree = new int[numCourses];
        for (int[] pair:prerequisites) {
            g.get(pair[0]).add(pair[1]);
            inDegree[pair[1]]++;
        }
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i=0;i<numCourses;++i) {
            if(inDegree[i]==0) {
                queue.add(i);
                ++cnt;
            }
        }
        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int x: g.get(u)) {
                if(--inDegree[x]==0) {
                    queue.add(x);
                    ++cnt;
                }
            }
        }
        return cnt==numCourses;
    }

    public static void main(String[] args) {
        int[][] numss = {{2,3,1,1,4},{3,2,1,0,4},{2,1,3},{1,2,1,0,3},{1}};
        for (int[] nums: numss) {
            System.out.println();
        }
    }
}

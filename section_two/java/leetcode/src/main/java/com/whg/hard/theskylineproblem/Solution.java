package com.whg.hard.theskylineproblem;

import java.util.*;
/**
 * Solution class
 *
 * problemId 218
 * @author wanghaogang
 * @date 2018/6/17
 */

public class Solution {
    public List<int[]> getSkyline(int[][] buildings) {
        List<int[]> ans = new ArrayList<>();
        List<Integer> position = new ArrayList<>();
        Set<Integer> vis = new HashSet<>();
        for (int[] x:buildings) {
            if(!vis.contains(x[0])) {
                position.add(x[0]);
                vis.add(x[0]);
            }
            if(!vis.contains(x[1])) {
                position.add(x[1]);
                vis.add(x[1]);
            }
        }
        Collections.sort(position);
        PriorityQueue<int[]> queue = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                if(o1[0]==o2[0]) {
                    return o1[1]-o2[1];
                } else {
                    return o2[0]-o1[0];
                }
            }
        });
        int i=0;
        for(int p: position) {
            while(i<buildings.length&&buildings[i][0]<=p) {
                queue.add(new int[]{buildings[i][2],buildings[i][1]});
                ++i;
            }
            while(!queue.isEmpty()&&queue.peek()[1]<=p) {
                queue.poll();
            }
            int h=!queue.isEmpty()?queue.peek()[0]:0;
            if (ans.size()==0||ans.get(ans.size()-1)[1]!=h) {
                ans.add(new int[]{p,h});
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        int[][] buildings={{2,9,10},{3,7,15},{5,12,12},{15,20,10},{19,24,8}};
        List<int[]> ans = new Solution().getSkyline(buildings);
        for (int[] x: ans) {
            System.out.println("("+x[0]+","+x[1]+")");
        }
    }
}

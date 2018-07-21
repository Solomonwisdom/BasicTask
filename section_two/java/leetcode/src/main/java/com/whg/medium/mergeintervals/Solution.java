package com.whg.medium.mergeintervals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Solution class
 *
 * problemId 56
 * @author wanghaogang
 * @date 2018/6/25
 */
public class Solution {

    public class Interval {
        int start;
        int end;

        Interval() {
            start = 0;
            end = 0;
        }

        Interval(int s, int e) {
            start = s;
            end = e;
        }
    }

    public List<Interval> merge(List<Interval> intervals) {
        if (intervals==null||intervals.size()<=1) {
            return intervals;
        }
        Collections.sort(intervals, new Comparator<Interval>() {
            @Override
            public int compare(Interval o1, Interval o2) {
                return o1.start!=o2.start?o1.start-o2.start:o1.end-o2.end;
            }
        });
        List<Interval> ans = new ArrayList<>();
        Interval cur = intervals.get(0);
        for (int i=1;i<intervals.size();++i) {
            Interval tmp = intervals.get(i);
            if(cur.end>=tmp.start) {
                if(tmp.end>cur.end) {
                    cur.end=tmp.end;
                }
            } else {
                ans.add(cur);
                cur = tmp;
            }
        }
        ans.add(cur);
        return ans;
    }

    public static void main(String[] args) {

    }
}

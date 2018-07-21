package com.whg.hard.lrucache;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Solution class
 *
 * problemId 146
 * @author wanghaogang
 * @date 2018/6/15
 */

public class LRUCache {
    public class Pair {
        private int key;
        private int value;
        public Pair() {}
        public Pair(int key, int value) {
            this.key = key;
            this.value = value;
        }
        int getKey() {return key;}
        int getValue() {return value;}
    }
    private int capacity;
    private int size;
    private int cnt;
    private PriorityQueue<Pair> priorityQueue;
    private Map<Integer,Integer> mark = new HashMap<>();
    private Map<Integer,Integer> pairs = new HashMap<>();
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.cnt=0;
        priorityQueue = new PriorityQueue<>(new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                return o1.getValue()-o2.getValue();
            }
        });
    }

    public int get(int key) {
        if (!pairs.containsKey(key)) {
            return -1;
        } else {
            mark.put(key,++cnt);
            priorityQueue.add(new Pair(key,cnt));
            return pairs.get(key);
        }
    }

    public void put(int key, int value) {
        if (!pairs.containsKey(key)&&size >= capacity) {
            for (;;) {
                Pair tmp = priorityQueue.poll();
                if (mark.get(tmp.getKey()).equals(tmp.getValue())) {
                    pairs.remove(tmp.getKey());
                    mark.remove(tmp.getKey());
                    break;
                }
            }
            --size;
        }
        if(!pairs.containsKey(key)) ++size;
        pairs.put(key,value);
        mark.put(key,++cnt);
        priorityQueue.add(new Pair(key,cnt));
    }
}

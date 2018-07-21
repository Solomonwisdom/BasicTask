package com.whg.hard.mergeksortedlists;

import java.util.PriorityQueue;

/**
 * Solution class
 *
 * problemId 23
 * @author wanghaogang
 * @date 2018/6/17
 */

class Solution {
    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }
    public ListNode mergeKLists(ListNode[] lists) {
        if(lists==null||lists.length==0) {
            return null;
        }
        PriorityQueue<ListNode> queue = new PriorityQueue<>(((o1, o2) -> o1.val-o2.val));
        ListNode head = new ListNode(0);
        ListNode tail = head;
        for (ListNode node: lists) {
            if(node!=null) {
                queue.add(node);
            }
        }
        while (!queue.isEmpty()) {
            tail.next=queue.poll();
            tail=tail.next;
            if(tail.next!=null) {
                queue.add(tail.next);
            }
        }
        return head.next;
    }
}

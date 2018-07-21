package com.whg.easy.intersectionoftwolinkedlists;

import java.util.List;
import java.util.PriorityQueue;

/**
 * Solution class
 *
 * problemId 160
 * @author wanghaogang
 * @date 2018/6/29
 */

class Solution {

    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        if (headA==null||headB==null) {
            return null;
        }
        ListNode nodeA = headA,nodeB=headB;
        boolean flagA = false, flagB=false;
        for (;nodeA!=nodeB;) {
            if (nodeA==null) {
                if (flagA) {
                    return null;
                } else {
                    flagA=true;
                    nodeA=headB;
                }
            } else {
                nodeA=nodeA.next;
            }
            if (nodeB==null) {
                if (flagB) {
                    return null;
                } else {
                    flagB=true;
                    nodeB=headA;
                }
            } else {
                nodeB=nodeB.next;
            }
        }
        return nodeA;
    }
}

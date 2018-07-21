package com.whg.easy.mergetwosortedlists;

/**
 * Solution class
 *
 * problemId 21
 * @author wanghaogang
 * @date 2018/6/29
 */
public class Solution {

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }
        ListNode dummy = new ListNode(-1);
        ListNode curNode = dummy;
        while (l1 != null && l2 != null) {
            if (l1.val < l2.val) {
                curNode.next = l1;
                l1 = l1.next;
            } else {
                curNode.next = l2;
                l2 = l2.next;
            }
            curNode = curNode.next;
        }
        while (l1 != null) {
            curNode.next = l1;
            l1 = l1.next;
            curNode = curNode.next;
        }
        while (l2 != null) {
            curNode.next = l2;
            l2 = l2.next;
            curNode = curNode.next;
        }
        return dummy.next;
    }
}

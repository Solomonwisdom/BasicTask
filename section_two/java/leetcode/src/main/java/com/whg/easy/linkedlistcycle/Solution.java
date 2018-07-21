package com.whg.easy.linkedlistcycle;

/**
 * Solution class
 *
 * problemId 141
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

    public boolean hasCycle(ListNode head) {
        ListNode oneStep = head;
        ListNode twoStep = head;
        while (twoStep != null && twoStep.next != null) {
            oneStep = oneStep.next;
            twoStep = twoStep.next.next;
            if (oneStep == twoStep) {
                break;
            }
        }
        if (twoStep == null || twoStep.next == null) {
            return false;
        }
        return true;
    }
}

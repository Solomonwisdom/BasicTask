package com.whg.medium.linkedlistcycletwo;

/**
 * Solution class
 *
 * problemId 142
 * @author wanghaogang
 * @date 2018/6/25
 */
public class Solution {

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public ListNode detectCycle(ListNode head) {
        ListNode oneStep = head;
        ListNode twoStep = head;
        while (twoStep!=null&&twoStep.next!=null) {
            oneStep = oneStep.next;
            twoStep = twoStep.next.next;
            if(oneStep == twoStep) {
                break;
            }
        }
        if(twoStep==null||twoStep.next==null) {
            return null;
        }
        int len = 0;
        do {
            oneStep = oneStep.next;
            ++len;
        } while (oneStep!=twoStep);
        oneStep = twoStep = head;
        while (len!=0) {
            twoStep = twoStep.next;
            --len;
        }
        while (oneStep!=twoStep) {
            oneStep=oneStep.next;
            twoStep=twoStep.next;
        }
        return oneStep;
    }
}

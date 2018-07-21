package com.whg.easy.reverselinkedlist;

/**
 * Solution class
 *
 * problemId 234
 * @author wanghaogang
 * @date 2018/6/29
 */

class Solution {

    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }

    public ListNode reverseList(ListNode head) {
        if (head==null||head.next==null) {
            return head;
        }
        ListNode curNode = head;
        ListNode nextNode = head.next;
        head.next=null;
        while(nextNode!=null) {
            ListNode temp = nextNode.next;
            nextNode.next = curNode;
            curNode = nextNode;
            nextNode = temp;
        }
        return curNode;
    }

}

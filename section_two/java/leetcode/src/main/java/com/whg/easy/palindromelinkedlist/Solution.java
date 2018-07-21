package com.whg.easy.palindromelinkedlist;

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

    public boolean isPalindrome(ListNode head) {
        if (head==null || head.next == null) {
            return true;
        }
        int length = 0;
        ListNode curNode = head;
        while (curNode!=null) {
            ++length;
            curNode=curNode.next;
        }
        int numReverse = length/2;
        curNode = head;
        for (int i=0;i<numReverse;++i) {
            curNode=curNode.next;
        }
        ListNode nextNode = curNode.next;
        curNode.next=null;
        while(nextNode!=null) {
            ListNode temp = nextNode.next;
            nextNode.next = curNode;
            curNode = nextNode;
            nextNode = temp;
        }

        while(curNode!=null) {
            if (curNode.val != head.val) {
                return false;
            }
            curNode = curNode.next;
            head = head.next;
        }
        return true;
    }

}

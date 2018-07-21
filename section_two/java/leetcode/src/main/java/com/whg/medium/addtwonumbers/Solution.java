package com.whg.medium.addtwonumbers;

/**
 * Solution class
 *
 * problemId 2
 * @author wanghaogang
 * @date 2018/6/24
 */
public class Solution {

    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if(l1==null) {
            return l2;
        }
        if(l2==null) {
            return l1;
        }
        ListNode head = new ListNode(-1);
        ListNode tail = head;
        int pre = 0;
        for (;l1!=null&&l2!=null;l1=l1.next,l2=l2.next) {
            int tmp = l1.val+l2.val + pre;
            tail.next = new ListNode(tmp%10);
            pre = tmp/10;
            tail = tail.next;
        }
        while(l1!=null) {
            int tmp = l1.val + pre;
            tail.next = new ListNode(tmp%10);
            pre = tmp/10;
            tail = tail.next;
            l1=l1.next;
        }
        while(l2!=null) {
            int tmp = l2.val + pre;
            tail.next = new ListNode(tmp%10);
            pre = tmp/10;
            tail = tail.next;
            l2=l2.next;
        }
        while(pre!=0) {
            tail.next=new ListNode(pre%10);
            pre/=10;
            tail=tail.next;
        }
        return head.next;
    }
    private ListNode arrayToList(int[] a) {
        if (a==null||a.length==0) {
            return null;
        }
        ListNode head = new ListNode(a[0]);
        ListNode tail = head;
        for (int i=1;i<a.length;++i) {
            tail.next = new ListNode(a[i]);
            tail = tail.next;
        }
        return head;
    }
    public static void main(String[] args) {
        int[] a={5,3,5,5},b={5,6,4,4};
        Solution solution = new Solution();
        ListNode listNode = solution.addTwoNumbers(solution.arrayToList(a),solution.arrayToList(b));
        while(listNode!=null) {
            System.out.print(listNode.val+"->");
            listNode = listNode.next;
        }
        System.out.println();
    }
}

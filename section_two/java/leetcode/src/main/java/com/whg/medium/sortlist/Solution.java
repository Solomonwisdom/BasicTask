package com.whg.medium.sortlist;

/**
 * Solution class
 *
 * problemId 148
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

    public ListNode sortList(ListNode head) {
        if(head == null||head.next==null) {
            return head;
        }
        int len = 0;
        ListNode cur = head;
        while (cur!=null) {
            cur=cur.next;
            ++len;
        }
        return sortList(head, len);
    }
    private ListNode sortList(ListNode head, int len) {
        if(len == 1) {
            return head;
        }
        int leftLen = len>>1;
        int rightLen = len-leftLen;
        ListNode l2 = head;
        ListNode cur = head;
        int curLen = 0;
        while (curLen < leftLen-1){
            cur = cur.next;
            ++curLen;
        }
        l2 = cur.next;
        cur.next = null;
        return merge(sortList(head, leftLen), sortList(l2, rightLen));
    }
    private ListNode merge(ListNode l1,ListNode l2) {
        ListNode head,cur;
        if (l1.val<l2.val) {
            head = cur=l1;
            l1=l1.next;
        } else {
            head = cur = l2;
            l2 = l2.next;
        }
        while(l1!=null&&l2!=null) {
            if(l1.val<l2.val) {
                cur.next = l1;
                l1 = l1.next;
            } else {
                cur.next = l2;
                l2 = l2.next;
            }
            cur = cur.next;
        }
        while (l1!=null) {
            cur.next = l1;
            l1 = l1.next;
            cur = cur.next;
        }
        while (l2!=null) {
            cur.next = l2;
            l2 = l2.next;
            cur = cur.next;
        }
        return head;
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
        int[] a={8,7,6,5,4,3,2,1};
        Solution solution = new Solution();
        ListNode listNode = solution.sortList(solution.arrayToList(a));
        while(listNode!=null) {
            System.out.print(listNode.val+"->");
            listNode = listNode.next;
        }
        System.out.println();
    }
}

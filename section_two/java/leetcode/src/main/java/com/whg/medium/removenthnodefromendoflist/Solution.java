package com.whg.medium.removenthnodefromendoflist;


/**
 * Solution class
 *
 * problemId 19
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

    public ListNode removeNthFromEnd(ListNode head, int n) {
        if (n==0) {
            return head;
        }
        ListNode cur = head,p=null;
        int pos = 0;
        while(cur!=null) {
            if(pos>=n) {
                p=p==null?head:p.next;
            }
            cur = cur.next;
            ++pos;
        }
        if(pos==n) {
            head = head.next;
        } else {
            p.next = p.next.next;
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
        ListNode listNode = solution.removeNthFromEnd(solution.arrayToList(a),7);
        while(listNode!=null) {
            System.out.print(listNode.val+"->");
            listNode = listNode.next;
        }
        System.out.println();
    }
}

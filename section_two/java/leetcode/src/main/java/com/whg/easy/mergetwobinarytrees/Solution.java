package com.whg.easy.mergetwobinarytrees;

/**
 * Solution class
 *
 * problemId 617
 * @author wanghaogang
 * @date 2018/6/29
 */
public class Solution {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null) {
            return null;
        }
        if (t1 == null) {
            return t2;
        }
        if (t2 == null) {
            return t1;
        }
        t1.val += t2.val;
        t1.left=mergeTrees(t1.left, t2.left);
        t1.right=mergeTrees(t1.right, t2.right);
        return t1;
    }

}

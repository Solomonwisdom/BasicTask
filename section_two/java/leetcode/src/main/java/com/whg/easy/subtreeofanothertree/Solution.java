package com.whg.easy.subtreeofanothertree;

/**
 * Solution class
 *
 * problemId 572
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

    boolean isSame(TreeNode s, TreeNode t) {
        if (s == null && t == null) {
            return true;
        }
        if (s == null || t == null) {
            return false;
        }
        return s.val == t.val && isSame(s.left, t.left) && isSame(s.right, t.right);
    }

    public boolean isSubtree(TreeNode s, TreeNode t) {
        return s != null && (isSame(s, t) || isSubtree(s.left, t) || isSubtree(s.right, t));
    }

}

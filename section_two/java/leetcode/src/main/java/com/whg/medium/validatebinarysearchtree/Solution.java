package com.whg.medium.validatebinarysearchtree;


/**
 * Solution class
 *
 * problemId 98
 * @author wanghaogang
 * @date 2018/6/20
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

    private boolean isValidBST(TreeNode root, long min, long max) {
        if(root==null) {
            return true;
        }
        if(root.val<=min||root.val>=max) {
            return false;
        }
        return isValidBST(root.left,min,root.val)&&isValidBST(root.right,root.val,max);
    }
    public boolean isValidBST(TreeNode root) {
        if(root==null) {
            return true;
        }
        long inf = 0x3f3f3f3f3f3f3f3fL;
        return isValidBST(root,-inf,inf);
    }

}

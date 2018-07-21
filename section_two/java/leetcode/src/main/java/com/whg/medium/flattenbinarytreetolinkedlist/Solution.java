package com.whg.medium.flattenbinarytreetolinkedlist;

/**
 * Solution class
 *
 * problemId 105
 * @author wanghaogang
 * @date 2018/6/25
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
    public void flatten(TreeNode root) {
        TreeNode cur = root;
        while (cur!=null) {
            // 先将右子树变成左子树的最右节点的右子树，再把
            // 变换后的左子树变成右子树
            if(cur.left!=null) {
                TreeNode pre = cur.left;
                while (pre.right!=null) {
                    pre = pre.right;
                }
                pre.right = cur.right;
                cur.right = cur.left;
                cur.left = null;
            }
            cur = cur.right;
        }
    }

}

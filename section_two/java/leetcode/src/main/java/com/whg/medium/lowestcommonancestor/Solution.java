package com.whg.medium.lowestcommonancestor;

/**
 * Solution class
 *
 * problemId 236
 * @author wanghaogang
 * @date 2018/6/24
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

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root == null||root==p||root==q) {
            return root;
        }
        TreeNode left = lowestCommonAncestor(root.left, p ,q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if(left!=null&&right!=null) {
            return root;
        }
        if(left!=null) {
            return left;
        }
        if(right!=null) {
            return right;
        }
        return null;
    }
    public static void main(String[] args) {

    }
}

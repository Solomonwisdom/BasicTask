package com.whg.easy.maximumdepthofbinarytree;

/**
 * Solution class
 *
 * problemId 104
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

    public int maxDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

}

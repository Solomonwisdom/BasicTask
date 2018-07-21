package com.whg.easy.diameterofbinarytree;

/**
 * Solution class
 *
 * problemId 543
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

    private int[] getDepthAndDiameter(TreeNode root) {
        if (root == null) {
            return new int[]{0, 0};
        }
        int[] pair = new int[]{0, 0};
        int[] left = getDepthAndDiameter(root.left);
        int[] right = getDepthAndDiameter(root.right);
        pair[1] = Math.max(left[0] + right[0], Math.max(left[1], right[1]));
        pair[0] = Math.max(left[0], right[0]) + 1;
        return pair;
    }

    public int diameterOfBinaryTree(TreeNode root) {
        return getDepthAndDiameter(root)[1];
    }

}

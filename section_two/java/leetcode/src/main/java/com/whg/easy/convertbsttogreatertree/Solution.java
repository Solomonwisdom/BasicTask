package com.whg.easy.convertbsttogreatertree;

/**
 * Solution class
 *
 * problemId 538
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

    private int convertBST(TreeNode root, int add) {
        if (root == null) {
            return 0;
        }
        int tmp = root.val;
        int rightSum = convertBST(root.right, add);
        root.val += rightSum + add;
        int leftSum = convertBST(root.left, add + rightSum + tmp);
        return leftSum + tmp + rightSum;
    }

    public TreeNode convertBST(TreeNode root) {
        convertBST(root, 0);
        return root;
    }

}

package com.whg.medium.houserobberthree;

/**
 * Solution class
 *
 * problemId 337
 * @author wanghaogang
 * @date 2018/6/28
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

    private int[] dfs(TreeNode root) {
        if (root==null) {
            return new int[2];
        }
        int[] left = dfs(root.left);
        int[] right = dfs(root.right);
        int[] ans = new int[2];
        ans[0]=Math.max(left[0],left[1])+Math.max(right[0],right[1]);
        ans[1]=root.val+left[0]+right[0];
        return ans;
    }

    public int rob(TreeNode root) {
        int[] ans = dfs(root);
        return Math.max(ans[0],ans[1]);
    }

}

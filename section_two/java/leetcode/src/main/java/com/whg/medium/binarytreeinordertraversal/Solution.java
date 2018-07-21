package com.whg.medium.binarytreeinordertraversal;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Solution class
 *
 * problemId 94
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

    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> ans = new ArrayList<>();
        TreeNode cur = root;
        Stack<TreeNode> stack = new Stack<>();
        while(cur!=null||!stack.isEmpty()) {
            while (cur!=null) {
                stack.push(cur);
                cur=cur.left;
            }
            TreeNode parent = stack.pop();
            ans.add(parent.val);
            if (parent.right!=null) {
                cur = parent.right;
            }
        }
        return ans;
    }

}

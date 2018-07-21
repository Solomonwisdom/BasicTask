package com.whg.medium.binarytreelevelordertraversal;

import java.util.*;

/**
 * Solution class
 *
 * problemId 102
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


    public List<List<Integer>> levelOrder(TreeNode root) {
        if (root == null) {
            return new ArrayList<>();
        }
        class Pair {
            TreeNode treeNode;
            int level;

            Pair(TreeNode treeNode, int level) {
                this.treeNode = treeNode;
                this.level = level;
            }
        }
        LinkedList<Pair> treeNodeQueue = new LinkedList<>();
        treeNodeQueue.add(new Pair(root, 0));
        List<List<Integer>> ans = new ArrayList<>();
        List<Integer> curList = new ArrayList<>();
        int curLevel = 0;
        while (!treeNodeQueue.isEmpty()) {
            Pair curPair = treeNodeQueue.getFirst();
            TreeNode curNode = curPair.treeNode;
            treeNodeQueue.removeFirst();
            if (curLevel != curPair.level) {
                ans.add(curList);
                curList = new ArrayList<>();
                curLevel = curPair.level;
            }
            curList.add(curNode.val);
            if (curNode.left != null) {
                treeNodeQueue.add(new Pair(curNode.left, curPair.level + 1));
            }
            if (curNode.right != null) {
                treeNodeQueue.add(new Pair(curNode.right, curPair.level + 1));
            }
        }
        ans.add(curList);
        return ans;
    }

}

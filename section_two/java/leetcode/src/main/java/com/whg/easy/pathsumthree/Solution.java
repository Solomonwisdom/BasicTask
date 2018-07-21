package com.whg.easy.pathsumthree;

import java.util.*;

/**
 * Solution class
 *
 * problemId 437
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

    private int pathSum(TreeNode root, int sum, int curSum, Map<Integer, Integer> map) {
        if (root == null) {
            return 0;
        }
        curSum += root.val;
        int ans = 0;
        if (map.containsKey(curSum - sum)) {
            ans += map.get(curSum - sum);
        }
        map.put(curSum, map.getOrDefault(curSum, 0) + 1);
        ans += pathSum(root.left, sum, curSum, map) + pathSum(root.right, sum, curSum, map);
        map.put(curSum, map.get(curSum) - 1);
        return ans;
    }

    public int pathSum(TreeNode root, int sum) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1);
        return pathSum(root, sum, 0, map);
    }

}

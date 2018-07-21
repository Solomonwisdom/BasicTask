package com.whg.hard.binarytreemaximumpathsum;

/**
 * Solution class
 *
 * problemId 124
 * @author wanghaogang
 * @date 2018/6/17
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
    private int ans=-(int)1e9;
    private int dfs(TreeNode u) {
        if(u==null) {
            return 0;
        }
        int l=dfs(u.left);
        int r=dfs(u.right);
        if(l<0) {
            l=0;
        }
        if(r<0) {
            r=0;
        }
        if(l+r+u.val>ans) {
            ans=l+r+u.val;
        }
        return u.val+(l>r?l:r);
    }
    public int maxPathSum(TreeNode root) {
        dfs(root);
        return ans;
    }
    public static void main(String[] args) {
        Integer[] arr={1,2,3};
        Solution solution = new Solution();
        TreeNode[] nodes = new TreeNode[arr.length];
        for(int i=0;i<arr.length;++i) {
            if(arr[i]!=null) {
                nodes[i]=solution.new TreeNode(arr[i]);
            }
        }
        for(int i=0;i<arr.length;++i) {
            if(arr[i]!=null) {
                if(2*i+1<arr.length&&arr[2*i+1]!=null) {
                    nodes[i].left=nodes[2*i+1];
                }
                if(2*i+2<arr.length&&arr[2*i+2]!=null) {
                    nodes[i].right=nodes[2*i+2];
                }
            }
        }
        System.out.println(solution.maxPathSum(nodes[0]));
    }
}
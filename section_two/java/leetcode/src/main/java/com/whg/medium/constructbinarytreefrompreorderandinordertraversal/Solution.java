package com.whg.medium.constructbinarytreefrompreorderandinordertraversal;

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
    private TreeNode build(int[] preorder, int pst,int ped, int[] inorder, int ist, int ied) {
        int tr=preorder[pst];
        TreeNode root = new TreeNode(tr);
        for(int i=ist;i<=ied;i++){
            if(inorder[i]==tr){
                if(i!=ist){
                    root.left=build(preorder, pst+1,pst+i-ist,inorder, ist,i-1);
                }
                if(i!=ied){
                    root.right=build(preorder,pst+i-ist+1,ped,inorder,i+1,ied);
                }
                break;
            }
        }
        return root;
    }

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder==null||preorder.length==0) {
            return null;
        }
        return build(preorder,0,preorder.length-1,inorder,0,inorder.length-1);
    }

}

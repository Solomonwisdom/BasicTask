package com.whg.hard.serializeanddeserializebinarytree;

import java.util.ArrayList;
import java.util.List;
/**
 * Solution class
 *
 * problemId 297
 * @author wanghaogang
 * @date 2018/6/18
 */
public class Codec {

    public class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    private String preStr(TreeNode root) {
        if(root==null) {
            return "*";
        }
        StringBuilder ret = new StringBuilder();
        ret.append(root.val);
        ret.append(",");
        ret.append(preStr(root.left));
        ret.append(",");
        ret.append(preStr(root.right));
        return ret.toString();
    }
    public class Pair {
        TreeNode first;
        int second;
        Pair(TreeNode first,int second) {
            this.first=first;
            this.second=second;
        }
    }
    private Pair parse(String[] data, int cur) {
        String nullChar = "*";
        if(data==null||cur>=data.length||nullChar.equals(data[cur])) {
            return new Pair(null,cur+1);
        }
        TreeNode root = new TreeNode(Integer.parseInt(data[cur]));
        Pair pair = parse(data, cur+1);
        root.left=pair.first;
        pair = parse(data, pair.second);
        root.right=pair.first;
        pair.first=root;
        return pair;
    }
    public String serialize(TreeNode root) {
        return preStr(root);
    }

    public TreeNode deserialize(String data) {
        return parse(data.split(","),0).first;
    }

    public static void main(String[] args) {
        Integer[] arr={3,2,4,3};
        Codec codec = new Codec();
        TreeNode[] nodes = new TreeNode[arr.length];
        for(int i=0;i<arr.length;++i) {
            if(arr[i]!=null) {
                nodes[i]=codec.new TreeNode(arr[i]);
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
        System.out.println(codec.serialize(nodes[0]));
        System.out.println(codec.serialize(codec.deserialize(codec.serialize(nodes[0]))));
    }
}

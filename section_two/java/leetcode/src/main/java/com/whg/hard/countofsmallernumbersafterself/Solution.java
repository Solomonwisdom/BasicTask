package com.whg.hard.countofsmallernumbersafterself;

import java.util.*;

/**
 * Solution class
 *
 * problemId 315
 * @author wanghaogang
 * @date 2018/6/18
 */
public class Solution {
    private int sum(int[] b,int x) {
        int ans=0;
        while(x>0) {
            ans+=b[x];
            x-=x&(-x);
        }
        return ans;
    }
    private void add(int[] b,int x,int d) {
        while(x<=b.length-1) {
            b[x]+=d;
            x+=x&(-x);
        }
    }
    public List<Integer> countSmaller(int[] nums) {
        if(nums==null||nums.length==0) {
            return new ArrayList<>();
        }
        int[] arr=new int[nums.length];
        int[] bt=new int[nums.length+1];
        int[] tmps = nums.clone();
        Arrays.sort(tmps);
        Map<Integer,Integer> map = new HashMap<>(nums.length);
        for (int i=0,j=0; i<nums.length; ++i) {
            if(i==0||tmps[i]!=tmps[i-1]) {
                map.put(tmps[i],++j);
            }
        }
        for (int i=nums.length-1; i>=0; --i) {
            int x=map.get(nums[i]);
            add(bt,x,1);
            arr[i]=sum(bt,x-1);
        }
        List<Integer> ans = new ArrayList<>();
        for (int i=0; i<nums.length; ++i) {
            ans.add(arr[i]);
        }
        return ans;
    }
    public static String listToString(List<Integer> list) {
        String s="[";
        for (int x:list) {
            s+=x+",";
        }
        return s+"]";
    }
    public static void main(String[] args) {
        int[] nums = new int[]{5,2,6,1};
        System.out.println(listToString(new Solution().countSmaller(nums)));
    }
}

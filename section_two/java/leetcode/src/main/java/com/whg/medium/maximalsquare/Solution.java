package com.whg.medium.maximalsquare;

/**
 * Solution class
 *
 * problemId 221
 * @author wanghaogang
 * @date 2018/6/25
 */
public class Solution {

    private int max(int a,int b) {
        return a>b?a:b;
    }
    private int min(int a,int b) {
        return a>b?b:a;
    }
    public int maximalSquare(char[][] matrix) {
        if(matrix==null||matrix.length==0) {
            return 0;
        }
        int m=matrix.length,n=matrix[0].length;
        if(n==0) {
            return 0;
        }
        int[][] up=new int[m][n],left=new int[m][n],right=new int[m][n];
        int ans=0;
        for (int i=0; i<m; ++i) {
            int lo=-1,ro=n;
            for (int j=0; j<n; ++j) {
                if(matrix[i][j]=='0') {
                    up[i][j]=left[i][j]=0;
                    lo=j;
                } else {
                    up[i][j]=i==0?1:up[i-1][j]+1;
                    left[i][j]=i==0?lo+1:max(left[i-1][j],lo+1);
                }
            }
            for (int j=n-1; j>=0; --j) {
                if(matrix[i][j]=='0') {
                    right[i][j]=n;
                    ro=j;
                } else {
                    right[i][j]=i==0?ro-1:min(right[i-1][j],ro-1);
                    int tmp = min(up[i][j], right[i][j]-left[i][j]+1);
                    ans=max(ans,tmp*tmp);
                }
            }
        }
        return ans;
    }
    public static void main(String[] args) {
        char[][] buildings={{'1','0','1','0','0'},{'1','0','1','1','1'},{'1','1','1','1','1'},{'1','0','0','1','0'}};
        int ans = new Solution().maximalSquare(buildings);
        System.out.println(""+ans);
    }
}

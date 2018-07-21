package com.whg.medium.rotateimage;

import java.util.Arrays;

/**
 * Solution class
 *
 * problemId 48
 * @author wanghaogang
 * @date 2018/6/28
 */
public class Solution {

    public void rotate(int[][] matrix) {
        if (matrix==null||matrix.length==0||matrix[0]==null) {
            return;
        }
        int n= matrix.length;
        for (int l=0,r=n-1;l<r;++l,--r) {
            for (int y=l;y<r;++y) {
                int tmp = matrix[r-(y-l)][l];
                matrix[r-(y-l)][l]=matrix[r][r-(y-l)];
                matrix[r][r-(y-l)]=matrix[y][r];
                matrix[y][r]=matrix[l][y];
                matrix[l][y]=tmp;
            }
        }
    }

    public static void main(String[] args) {
        int[][] matrix={{1,2,3},{4,5,6},{7,8,9}};
        new Solution().rotate(matrix);
        for (int[] m:matrix) {
            for (int x:m) {
                System.out.print(x+",");
            }
            System.out.println();
        }
    }
}

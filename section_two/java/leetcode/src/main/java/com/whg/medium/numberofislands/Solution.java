package com.whg.medium.numberofislands;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution class
 *
 * problemId 200
 * @author wanghaogang
 * @date 2018/6/26
 */
public class Solution {

    private void dfs(int x,int y,char[][] grid,boolean[][] vis,int[][] dir) {
        vis[x][y]=true;
        for (int i=0;i<dir.length;++i) {
            int nx = x+dir[i][0],ny=y+dir[i][1];
            if(nx<0||nx>=grid.length||ny<0||ny>=grid[0].length||vis[nx][ny]||grid[nx][ny]!='1') {
                continue;
            }
            dfs(nx,ny,grid,vis,dir);
        }
    }
    public int numIslands(char[][] grid) {
        if (grid==null||grid.length==0||grid[0]==null||grid[0].length==0) {
            return 0;
        }
        int[][] dir={{1,0},{-1,0},{0,1},{0,-1}};
        boolean[][] vis = new boolean[grid.length][grid[0].length];
        int ans=0;
        for (int i=0;i<grid.length;++i) {
            for (int j=0;j<grid[i].length;++j) {
                if(!vis[i][j]&&grid[i][j]=='1') {
                    ++ans;
                    dfs(i,j,grid,vis,dir);
                }
            }
        }
        return ans;
    }
}

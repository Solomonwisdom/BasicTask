package com.whg.medium.minimumpathsum;

/**
 * Solution class
 *
 * problemId 64
 * @author wanghaogang
 * @date 2018/6/27
 */
public class Solution {

    private int dfs(int x, int y, int[][] grid, int[][] mem) {
        if (mem[x][y] != -1) {
            return mem[x][y];
        }
        int ans = -1;
        if (x + 1 < grid.length) {
            ans = dfs(x + 1, y, grid, mem);
        }
        if (y + 1 < grid[x].length) {
            int tmp = dfs(x, y + 1, grid, mem);
            if (ans == -1 || ans > tmp) {
                ans = tmp;
            }
        }
        if (ans == -1) {
            ans = 0;
        }
        mem[x][y] = ans + grid[x][y];
        return mem[x][y];
    }

    public int minPathSum(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        int[][] mem = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; ++i) {
            for (int j = 0; j < grid[i].length; ++j) {
                mem[i][j] = -1;
            }
        }
        return dfs(0, 0, grid, mem);
    }

    public static void main(String[] args) {
        int[][] grid = {{1, 3, 1}, {1, 5, 1}, {4, 2, 1}};
        System.out.println(new Solution().minPathSum(grid));
    }
}

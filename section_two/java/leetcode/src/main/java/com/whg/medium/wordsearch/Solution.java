package com.whg.medium.wordsearch;

/**
 * Solution class
 *
 * problemId 79
 * @author wanghaogang
 * @date 2018/6/20
 */
public class Solution {


    private int[] dx = {-1,0,1,0};
    private int[] dy = {0,1,0,-1};
    boolean dfs(char[][] board, String word, int k, boolean[][] vis, int x,int y) {
        if(k==word.length()) {
            return true;
        }
        for (int i=0;i<dx.length;++i) {
            int nx = x+dx[i],ny=y+dy[i];
            if(nx<0||nx>=board.length||ny<0||ny>=board[x].length||vis[nx][ny]) {
                continue;
            }
            if(board[nx][ny]==word.charAt(k)) {
                vis[nx][ny]=true;
                if (dfs(board,word,k+1,vis,nx,ny)) {
                    return true;
                }
                vis[nx][ny]=false;
            }
        }
        return false;
    }
    public boolean exist(char[][] board, String word) {
        if(word==null||"".equals(word)) {
            return true;
        }
        if(board==null||board.length==0||board[0].length==0) {
            return false;
        }
        boolean[][] vis = new boolean[board.length][board[0].length];
        for (int i=0; i<board.length; ++i) {
            for (int j=0; j<board[i].length; ++j) {
                if(board[i][j]==word.charAt(0)) {
                    vis[i][j]=true;
                    if(dfs(board, word, 1, vis,i, j)) {
                        return true;
                    }
                    vis[i][j]=false;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        char[][] board = {
                {'A', 'B', 'C', 'E'},
                {'S', 'F', 'C', 'S'},
                {'A', 'D', 'E', 'E'}};
        String word = "ABCE";
        System.out.println(new Solution().exist(board,word));
    }
}

"""
 Solution class

 problemId 72
 @author wanghaogang
 @date 2018/6/29

"""


class Solution:
    def minDistance(self, word1, word2):
        """
        :type word1: str
        :type word2: str
        :rtype: int
        """
        n = len(word1)
        m = len(word2)
        dp = [[0 for i in range(m+1)] for j in range(n+1)]
        for j in range(1, m+1):
            dp[0][j] = j
        for i in range(1, n+1):
            dp[i][0] = i
            for j in range(1, m+1):
                if word1[i-1] == word2[j-1]:
                    dp[i][j] = dp[i-1][j-1]
                else:
                    dp[i][j] = dp[i-1][j-1]+1
                dp[i][j] = min(dp[i][j], dp[i-1][j]+1)
                dp[i][j] = min(dp[i][j], dp[i][j-1]+1)
        return dp[n][m]
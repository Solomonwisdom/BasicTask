"""
 Solution class

 problemId 32
 @author wanghaogang
 @date 2018/6/29

"""


class Solution:
    def longestValidParentheses(self, s):
        """
        :type s: str
        :rtype: int
        """
        ans = 0
        dp = [0 for i in range(len(s) + 1)]
        for i in range(1, len(s) + 1):
            if i >= 2 and s[i-1] == ')':
                if s[i-2] == '(':
                    dp[i] = dp[i-2]+2
                elif i-dp[i-1] > 1 and s[i-dp[i-1]-2] == '(':
                    dp[i] = dp[i-1]+2+dp[i-dp[i-1]-2]
            if ans < dp[i]:
                ans = dp[i]
        return ans

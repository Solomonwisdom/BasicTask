"""
 Solution class

 problemId 312
 @author wanghaogang
 @date 2018/6/29

"""


class Solution:
    def maxCoins(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        if not nums or n == 0:
            return 0
        dp = [[0 for i in range(n)] for j in range(n)]
        for length in range(1, n+1):
            for left in range(n-length+1):
                right = left + length - 1
                for j in range(left, right+1):
                    tmp = (1 if left < 1 else nums[left-1])*nums[j]*(1 if right + 1 >= n else nums[right+1])
                    if j > left:
                        tmp += dp[left][j-1]
                    if j< right:
                        tmp += dp[j+1][right]
                    dp[left][right] = max(tmp, dp[left][right])
        return dp[0][n - 1]

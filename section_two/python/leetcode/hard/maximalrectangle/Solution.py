"""
 Solution class

 problemId 85
 @author wanghaogang
 @date 2018/6/29

"""


class Solution:
    def maximalRectangle(self, matrix):
        """
        :type matrix: List[List[str]]
        :rtype: int
        """
        if not matrix or len(matrix) == 0:
            return 0
        m = len(matrix)
        n = len(matrix[0])
        if n == 0:
            return 0
        up = [[0 for i in range(n)] for j in range(m)]
        left = [[0 for i in range(n)] for j in range(m)]
        right = [[0 for i in range(n)] for j in range(m)]
        ans = 0
        for i in range(m):
            lo = -1
            ro = n
            for j in range(n):
                if matrix[i][j] == '0':
                    up[i][j] = left[i][j] = 0
                    lo = j
                else:
                    up[i][j] = 1 if i == 0 else up[i-1][j]+1
                    left[i][j] = lo+1 if i == 0 else max(left[i - 1][j], lo + 1)
            for j in range(n-1, -1, -1):
                if matrix[i][j] == '0':
                    right[i][j] = n
                    ro = j
                else:
                    right[i][j] = ro-1 if i == 0 else min(right[i-1][j], ro - 1)
                    ans = max(ans, up[i][j]*(right[i][j]-left[i][j]+1))
        return ans


solution = Solution()
print(solution.maximalRectangle([["1","0","1","0","0"],["1","0","1","1","1"],["1","1","1","1","1"],["1","0","0","1","0"]]
))

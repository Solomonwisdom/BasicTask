"""
 Solution class

 problemId 84
 @author wanghaogang
 @date 2018/6/29

"""


class Solution:
    def largestRectangleArea(self, heights):
        """
        :type heights: List[int]
        :rtype: int
        """
        stack = [0 for i in range(len(heights)+1)]
        top = 0
        ans = 0
        i = 0
        while i <= len(heights):
            h = heights[i] if i < len(heights) else 0
            if top == 0 or heights[stack[top-1]] <= h:
                stack[top] = i
                top += 1
            else:
                top -= 1
                tmp = stack[top]
                ans = max(ans, heights[tmp] * (i if top == 0 else i - 1 - stack[top-1]))
                i -= 1
            i += 1
        return ans


solution = Solution()
print(solution.largestRectangleArea([2, 1, 5, 6, 2, 3]))

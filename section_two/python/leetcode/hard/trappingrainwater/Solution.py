"""
 Solution class

 problemId 42
 @author wanghaogang
 @date 2018/6/29

"""


class Solution:
    def trap(self, height):
        """
        :type height: List[int]
        :rtype: int
        """
        if not height or len(height) == 0:
            return 0
        left = [0 for i in range(len(height))]
        right = [0 for i in range(len(height))]
        left[0] = height[0]
        length = len(height)
        right[length - 1] = height[length - 1]
        for i in range(1, length):
            left[i] = max(left[i - 1], height[i])
        for i in range(length-2, -1, -1):
            right[i] = max(right[i + 1], height[i])
        ans = 0
        for i in range(length):
            ans += min(left[i], right[i]) - height[i]
        return ans
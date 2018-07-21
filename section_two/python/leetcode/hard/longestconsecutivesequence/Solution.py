"""
 Solution class

 problemId 128
 @author wanghaogang
 @date 2018/6/29

"""


class Solution:
    def longestConsecutive(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        length = len(nums)
        if not nums or length == 0:
            return 0
        s = set(nums)
        ans = 0
        cur = 0
        for x in nums:
            if not x - 1 in s:
                cur = 1
                i = 1
                while x + i in s:
                    cur += 1
                    i += 1
                ans = max(ans, cur)
        return ans

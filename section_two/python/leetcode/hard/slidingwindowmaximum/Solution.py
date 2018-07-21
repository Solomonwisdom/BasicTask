"""
 Solution class

 problemId 239
 @author wanghaogang
 @date 2018/6/29

"""


class Solution:
    def maxSlidingWindow(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: List[int]
        """
        if not nums or len(nums) == 0:
            return []
        pQueue = [0 for i in range(len(nums))]
        ans = [0 for i in range(len(nums)-k+1)]
        left = 0
        right = 0
        for i in range(len(nums)):
            if i < k:
                while right > left and nums[pQueue[right - 1]] <= nums[i]:
                    right -= 1
            else:
                ans[i - k] = nums[pQueue[left]]
                if pQueue[left] == i - k:
                    left += 1
                while right > left and nums[pQueue[right - 1]] <= nums[i]:
                    right -= 1
            pQueue[right] = i
            right += 1
        ans[len(nums) - k] = nums[pQueue[left]]
        return ans

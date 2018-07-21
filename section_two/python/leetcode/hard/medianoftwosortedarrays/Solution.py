"""
 Solution class

 problemId 4
 @author wanghaogang
 @date 2018/6/29

"""


class Solution:
    def findMedianSortedArrays(self, nums1, nums2):
        """
        :type nums1: List[int]
        :type nums2: List[int]
        :rtype: float
        """
        length = len(nums1) + len(nums2)
        i = 0
        j = 0
        k = 0
        posA = (length+1)//2
        posB = -1
        if length % 2 == 0:
            posB = posA+1
        ans = 0.0
        while i < len(nums1) and j < len(nums2):
            k += 1
            if k > posA and k > posB:
                break
            if nums1[i] < nums2[j]:
                if k == posA or k == posB:
                    ans += nums1[i]

                i += 1
            else:
                if k == posA or k == posB:
                    ans += nums2[j]
                j += 1
        if k > posA and k > posB:
            if posB != -1:
                ans /= 2
            return ans
        while i < len(nums1):
            k += 1
            if k > posA and k > posB:
                break
            if k == posA or k == posB:
                ans += nums1[i]
            i += 1
        while j < len(nums2):
            k += 1
            if k > posA and k > posB:
                break
            if k == posA or k == posB:
                ans += nums2[j]
            j += 1
        if posB != -1:
            ans /= 2
        return ans

"""
 Solution class

 problemId 76
 @author wanghaogang
 @date 2018/6/29

"""


class Solution:
    def minWindow(self, s, t):
        """
        :type s: str
        :type t: str
        :rtype: str
        """
        if len(s) == 0:
            return ''
        left = 0
        right = 0
        cnt = 0
        tCnt = [0 for i in range(300)]
        sCnt = tCnt.copy()
        for i in range(len(t)):
            tCnt[ord(t[i])] += 1
        ansL = -1
        ansR = -1
        ansLen = len(s) + 5
        while right < len(s):
            sCnt[ord(s[right])] += 1
            if sCnt[ord(s[right])] <= tCnt[ord(s[right])]:
                cnt += 1
                while cnt == len(t):
                    if right-left+1 < ansLen:
                        ansLen = right - left + 1
                        ansL = left
                        ansR = right
                    sCnt[ord(s[left])] -= 1
                    if sCnt[ord(s[left])] < tCnt[ord(s[left])]:
                        cnt -= 1
                    left += 1
            right += 1
        if ansL == -1:
            return ''
        else:
            return s[ansL:ansR + 1]

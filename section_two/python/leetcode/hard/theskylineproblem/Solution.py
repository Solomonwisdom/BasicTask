"""
 Solution class

 problemId 218
 @author wanghaogang
 @date 2018/6/29

"""

from heapq import heappush, heappop


class Solution:
    def getSkyline(self, buildings):
        """
        :type buildings: List[List[int]]
        :rtype: List[List[int]]
        """
        ans = []
        position = set([b[0] for b in buildings] + [b[1] for b in buildings])
        cur = []
        i = 0
        for p in sorted(position):
            while i < len(buildings) and buildings[i][0] <= p:
                heappush(cur, (-buildings[i][2], buildings[i][1]))
                i += 1
            while len(cur) > 0 and cur[0][1] <= p:
                heappop(cur)
            h = -cur[0][0] if len(cur) > 0 else 0
            if len(ans) == 0 or ans[-1][1] != h:
                ans.append([p, h])
        return ans

"""
 Solution class

 problemId 10
 @author wanghaogang
 @date 2018/6/29

"""


class Solution:
    def isMatch(self, s, p):
        """
        :type s: str
        :type p: str
        :rtype: bool
        """
        self.mem = [[-1 for i in range(len(p)+1)] for j in range(len(s)+1)]
        return self.dp(0, 0, s, p)

    def dp(self, i, j, s, p):
        if self.mem[i][j] != -1:
            return self.mem[i][j] > 0
        if j == len(p):
            ans = i == len(s)
        else:
            firstMatch = (i < len(s) and (p[j] == s[i] or p[j] == '.'))
            if j + 1 < len(p) and p[j + 1] == '*':
                ans = self.dp(i, j+2, s, p) or (firstMatch and self.dp(i+1, j, s, p))
            else:
                ans = firstMatch and self.dp(i+1, j+1, s, p)
        self.mem[i][j] = ans
        return ans

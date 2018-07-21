"""
 Solution class

 problemId 301
 @author wanghaogang
 @date 2018/6/29

"""


class Solution:
    def removeInvalidParentheses(self, s):
        """
        :type s: str
        :rtype: List[str]
        """
        self.ans = []
        self.process(s, 0, 0, ('(', ')'))
        return self.ans

    def process(self, s, last_i, last_j, par):
        stk = 0
        for i in range(last_i, len(s)):
            if s[i] == par[0]:
                stk += 1
            elif s[i] == par[1]:
                stk -= 1
            if stk < 0:
                for j in range(last_j, i+1):
                    if s[j] == par[1]:
                        if j == last_j or s[j-1] != par[1]:
                            self.process(s[:j] + s[j+1:], i, j, par)
                return
        rev = ''.join(reversed(s))
        if par[0] == '(':
            self.process(rev, 0, 0, (')', '('))
        else:
            self.ans.append(rev)

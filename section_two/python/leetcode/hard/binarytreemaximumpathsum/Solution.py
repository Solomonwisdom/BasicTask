"""
 Solution class

 problemId 124
 @author wanghaogang
 @date 2018/6/29

"""


class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None


class Solution:
    def maxPathSum(self, root):
        """
        :type root: TreeNode
        :rtype: int
        """
        self.ans = -0x80000000
        self.dfs(root)
        return self.ans

    def dfs(self, u):
        if not u :
            return 0
        l = self.dfs(u.left)
        r = self.dfs(u.right)
        if l < 0:
            l = 0
        if r < 0:
            r = 0
        if l + r + u.val > self.ans:
            self.ans = l + r + u.val
        if l > r:
            return u.val + l
        else:
            return u.val + r

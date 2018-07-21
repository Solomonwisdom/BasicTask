"""
 Solution class

 problemId 297
 @author wanghaogang
 @date 2018/6/29

"""


class TreeNode(object):
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None


class Codec:

    def serialize(self, root):
        """Encodes a tree to a single string.

        :type root: TreeNode
        :rtype: str
        """
        return self.pre_order_str(root)

    def deserialize(self, data):
        """Decodes your encoded data to tree.

        :type data: str
        :rtype: TreeNode
        """
        return self.parse(data.split(','), 0)[0]

    def pre_order_str(self, root):
        if not root:
            return "*"
        ret = str()
        ret += str(root.val)
        ret += ","
        ret += self.pre_order_str(root.left)
        ret += ","
        ret += self.pre_order_str(root.right)
        return ret

    def parse(self, data, cur):
        if not data or cur >= len(data) or '*' == data[cur]:
            return [None, cur + 1]
        root = TreeNode(int(data[cur]))
        pair = self.parse(data, cur + 1)
        root.left = pair[0]
        pair = self.parse(data, pair[1])
        root.right = pair[0]
        pair[0] = root
        return pair

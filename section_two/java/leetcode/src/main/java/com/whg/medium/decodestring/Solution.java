package com.whg.medium.decodestring;

/**
 * Solution class
 *
 * problemId 394
 * @author wanghaogang
 * @date 2018/6/27
 */
public class Solution {

    private String decodeString(String s, int start, int end, int[] rightPos) {
        int tmp = 0;
        StringBuilder ans = new StringBuilder();
        for (int i = start; i <= end; ++i) {
            if (Character.isDigit(s.charAt(i))) {
                do {
                    tmp = tmp * 10 + (s.charAt(i) - '0');
                    ++i;
                } while (Character.isDigit(s.charAt(i)));
                --i;
            } else if (s.charAt(i) == '[') {
                String part = decodeString(s, i + 1, rightPos[i] - 1, rightPos);
                for (int j = 0; j < tmp; ++j) {
                    ans.append(part);
                }
                tmp = 0;
                i = rightPos[i];
            } else {
                ans.append(s.charAt(i));
            }
        }
        return ans.toString();
    }

    public String decodeString(String s) {
        if (s==null||s.length()==0) {
            return "";
        }
        int[] rightPos = new int[s.length()];
        int[] stack = new int[s.length()];
        int top = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '[') {
                stack[top++] = i;
            } else if (s.charAt(i) == ']') {
                rightPos[stack[--top]] = i;
            }
        }
        return decodeString(s, 0, s.length() - 1, rightPos);
    }

    public static void main(String[] args) {
        String[] strs = {"3[a]2[bc]", "3[a2[c]]", "2[abc]3[cd]ef"};
        Solution solution = new Solution();
        for (String str : strs) {
            System.out.println(solution.decodeString(str));
        }
    }
}

package com.whg.medium.groupanagrams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Solution class
 *
 * problemId 49
 * @author wanghaogang
 * @date 2018/6/26
 */
public class Solution {

    public List<List<String>> groupAnagrams(String[] strs) {
        String[][] strPairs = new String[strs.length][2];
        for (int i=0; i<strs.length; ++i) {
            char[] tmpChars = strs[i].toCharArray();
            Arrays.sort(tmpChars);
            StringBuilder stringBuilder = new StringBuilder();
            for (char ch: tmpChars) {
                stringBuilder.append(ch);
            }
            strPairs[i][0]=stringBuilder.toString();
            strPairs[i][1]=strs[i];
        }
        Arrays.sort(strPairs, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return o1[0].compareTo(o2[0]);
            }
        });
        List<List<String>> ans = new ArrayList<>();
        List<String> cur = new ArrayList<>();
        for (int i=0;i<strPairs.length;++i) {
            if (i!=0&&!strPairs[i][0].equals(strPairs[i-1][0])) {
                ans.add(cur);
                cur = new ArrayList<>();
            }
            cur.add(strPairs[i][1]);
            if (i==strPairs.length-1) {
                ans.add(cur);
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        String[] strs = {"eat", "tea", "tan", "ate", "nat", "bat"};
        List<List<String>> ans = new Solution().groupAnagrams(strs);
        for (List<String> stringList: ans) {
            for (String str: stringList) {
                System.out.print(str+",");
            }
            System.out.println();
        }
    }
}

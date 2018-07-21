package com.whg.medium.lettercombinationsofaphonenumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Solution class
 *
 * problemId 17
 * @author wanghaogang
 * @date 2018/6/26
 */
public class Solution {

    private String[] mappings = {"abc","def","ghi","jkl","mno","pqrs","tuv","wxyz"};
    private void dfs(int cur,String digitis, StringBuilder tmp,List<String> ans) {
        if (cur==digitis.length()) {
            ans.add(tmp.toString());
            return;
        }
        String mapping = mappings[digitis.charAt(cur)-'2'];
        for (int i=0;i<mapping.length();++i) {
            tmp.append(mapping.charAt(i));
            dfs(cur+1,digitis,tmp,ans);
            tmp.deleteCharAt(cur);
        }
    }
    public List<String> letterCombinations(String digits) {
        List<String> ans = new ArrayList<>();
        if (digits==null||digits.length()==0) {
            return ans;
        }
        StringBuilder tmp = new StringBuilder();
        dfs(0,digits,tmp,ans);
        return ans;
    }
}

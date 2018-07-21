package com.whg.medium.implementtrie;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    private class TrieNode {
        TrieNode[] child = new TrieNode[26];
        boolean isMatch = false;
    }

    private List<TrieNode> trieNodes = new ArrayList<>(10000);

    /**
     * Initialize your data structure here.
     */
    public Trie() {
        trieNodes.add(new TrieNode());
    }

    /**
     * Inserts a word into the trie.
     */
    public void insert(String word) {
        TrieNode u = trieNodes.get(0);
        int n = word.length();
        for (int i = 0; i < n; i++) {
            int c = word.charAt(i) - 'a';
            if (u.child[c] == null) {
                TrieNode node = new TrieNode();
                trieNodes.add(node);
                u.child[c] = node;
            }
            u = u.child[c];
        }
        u.isMatch = true;
    }

    /**
     * Returns if the word is in the trie.
     */
    public boolean search(String word) {
        TrieNode u = trieNodes.get(0);
        int n = word.length();
        for (int i = 0; i < n; i++) {
            int c = word.charAt(i) - 'a';
            if (u.child[c] == null) {
                return false;
            }
            u = u.child[c];
        }
        return u.isMatch;
    }

    /**
     * Returns if there is any word in the trie that starts with the given prefix.
     */
    public boolean startsWith(String prefix) {
        TrieNode u = trieNodes.get(0);
        int n = prefix.length();
        for (int i = 0; i < n && u != null; i++) {
            int c = prefix.charAt(i) - 'a';
            u = u.child[c];
        }
        return u != null;
    }

    public static void main(String[] args) {
        Trie obj = new Trie();
        String word = "apple",prefix = "";
        obj.insert(word);
        System.out.println(obj.search(word));
        System.out.println(obj.startsWith(prefix));
    }
}

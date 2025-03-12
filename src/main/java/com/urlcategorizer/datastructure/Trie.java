package com.urlcategorizer.datastructure;

import java.util.HashMap;
import java.util.Map;

/**
 * Trie data structure for efficient keyword storage and lookup.
 */
public class Trie {
    private final TrieNode root;

    /**
     * Constructs an empty Trie.
     */
    public Trie() {
        this.root = new TrieNode();
    }

    /**
     * Inserts a keyword into the Trie.
     * @param word The keyword to insert.
     */
    public void insert(String word) {
        TrieNode current = root;
        for (char c : word.toCharArray()) {
            current = current.children.computeIfAbsent(c, k -> new TrieNode());
        }
        current.isEndOfWord = true;
    }

    /**
     * Checks if a given text is present in the Trie.
     * @param text The text to search for.
     * @return {@code true} if the keyword exists in the Trie, {@code false} otherwise.
     */
    public boolean contains(String text) {
        TrieNode current = root;
        for (char c : text.toCharArray()) {
            current = current.children.get(c);
            if (current == null) {
                return false;
            }
        }
        return current.isEndOfWord;
    }

    /**
     * Represents a node in the Trie.
     */
    private static class TrieNode {
        private final Map<Character, TrieNode> children = new HashMap<>();
        private boolean isEndOfWord = false;
    }
}

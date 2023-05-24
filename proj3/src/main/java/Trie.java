import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Trie Data Structure with "cleaned string"s as keys (see GraphDB.cleanString).
 * The value of a node (i.e., word) is the "raw string" of its "cleaned version".
 * Note that we use a list of strings in each node since there might be multiple
 * words with the same "cleaned version".
 */
public class Trie {

    private class TrieNode {
        boolean isKey;
        Map<Character, TrieNode> children;
        List<String> words;

        TrieNode() {
            isKey = false;
            children = new HashMap<>();
            words = new LinkedList<>();
        }
    }

    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    /**
     * Adds the word into "key"'s list.
     *
     * @param key  Cleaned version of raw string
     * @param word raw string
     */
    public void addWord(String key, String word) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null!");
        }

        TrieNode cur = root;
        for (int pos = 0; pos < key.length(); pos++) {
            char c = key.charAt(pos);
            if (!cur.children.containsKey(c)) {
                cur.children.put(c, new TrieNode());
            }
            cur = cur.children.get(c);
        }
        cur.isKey = true;
        cur.words.add(word);
    }

    /**
     * Returns words with the prefix "key".
     *
     * @param key The "cleaned version" of a word.
     * @return List of words, where their "cleaned version" has prefix "key".
     */
    public List<String> wordsWithPrefix(String key) {
        List<String> result = new LinkedList<>();
        TrieNode n = find(key);
        if (n != null) {
            wordsWithPrefix(result, n);
        }
        return result;
    }

    private void wordsWithPrefix(List<String> result, TrieNode n) {
        if (n.isKey) {
            result.addAll(n.words);
        }
        for (char c : n.children.keySet()) {
            TrieNode child = n.children.get(c);
            wordsWithPrefix(result, child);
        }
    }

    /**
     * Returns the found node with given key, returns null if not found.
     */
    private TrieNode find(String key) {
        TrieNode cur = root;
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (!cur.children.containsKey(c)) {
                return null;
            }
            cur = cur.children.get(c);
        }
        return cur;
    }

}

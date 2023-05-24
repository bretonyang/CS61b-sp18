import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.Set;
import java.util.HashSet;

public class TestTrie {
    @Test
    public void testAddAndPrefixWithKey() {
        Trie t = new Trie();
        t.addWord("cat", "cat");
        t.addWord("cat", "c123at!.");
        t.addWord("dog", "dOg$");
        t.addWord("bird", "bi!rd");
        t.addWord("captain", "!cApt4ain");
        Set<String> expected = new HashSet<>();
        expected.add("cat");
        expected.add("c123at!.");
        Set<String> actual = new HashSet<>(t.wordsWithPrefix("cat"));
        assertEquals(expected, actual);

        expected.add("!cApt4ain");
        actual = new HashSet<>(t.wordsWithPrefix("ca"));
        assertEquals(expected, actual);
    }
}

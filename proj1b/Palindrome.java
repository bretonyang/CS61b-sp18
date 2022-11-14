public class Palindrome {

    /**
     * Returns a deque where the characters appear in the
     * same order as in the String.
     */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> dq = new ArrayDeque<>();
        for (int i = 0; i < word.length(); i++) {
            dq.addLast(word.charAt(i));
        }
        return dq;
    }

    /**
     * Returns true if the given word is a palindrome, false
     * otherwise.
     * <br>
     * A palindrome is defined as a word that is the
     * same whether it is read forwards or backwards.
     * Any word of length 1 or 0 is a palindrome.
     */
    public boolean isPalindrome(String word) {
        Deque<Character> dq = wordToDeque(word);
        return isPalindromeHelper(dq);
    }

    private static boolean isPalindromeHelper(Deque<Character> dq) {
        if (dq.size() <= 1) {
            return true;
        }

        if (dq.removeFirst() == dq.removeLast()) {
            return isPalindromeHelper(dq);
        }
        return false;
    }

    /**
     * Return true if the word is a palindrome according to the character
     * comparison test provided by the CharacterComparator passed in as
     * argument cc, and false otherwise.
     * <br>
     * Any word of length 1 or 0 is a palindrome.
     */
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> dq = wordToDeque(word);
        return isPalindromeHelper(dq, cc);
    }

    private static boolean isPalindromeHelper(Deque<Character> dq, CharacterComparator cc) {
        if (dq.size() <= 1) {
            return true;
        }

        if (cc.equalChars(dq.removeFirst(), dq.removeLast())) {
            return isPalindromeHelper(dq, cc);
        }
        return false;
    }

}

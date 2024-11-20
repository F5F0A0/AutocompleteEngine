package autocompleteengine;

import java.util.ArrayList;

/**
 * Implements a De La Briandais (DLB) Trie structure (prefix tree) to store a dictionary of words.
 */
public class DLB {

    private DLBNode root;
    private DLBNode cur; // current search node
    private String suggestPrefix = ""; // builds a prefix string for suggestions

    /**
     * Adds a new word to the dictionary.
     *
     * @param key The word to be added.
     */
    public void add(String key) {
        if (key == null) {
            return; // if key is null do nothing
        }

        if (root == null) { // special case where root is null
            root = new DLBNode(key.charAt(0)); // initialize root node
        }

        DLBNode cur = root; // traversal reference

        for (int i = 0; i < key.length(); i++) { // for each letter in the word, we need to find the exact place to insert it in the chain
            char c = key.charAt(i); // get the letter we need to insert or validate that it's in the DLB

            // traverse the row until we've found the letter, or we reach the final entry in the row
            while (cur.getCharacter() != c && cur.getRight() != null) {
                cur = cur.getRight();
            }

            // if we reach the final entry and it doesn't equal the letter, set a new node to the right to our letter
            if (cur.getCharacter() != c) {
                cur.setRight(new DLBNode(c));
                cur = cur.getRight(); // update the positioning to traverse down
            }

            // if we have more letters to add, we must add them
            if (i < key.length() - 1) {
                if (cur.getDown() == null) { // add the next letter
                    cur.setDown(new DLBNode(key.charAt(i + 1)));
                }
                cur = cur.getDown(); // traverse down, and continue traversal
            }
            else { // else we need to add the null terminator if it doesn't exist yet
                if (cur.getDown() == null) {
                    cur.setDown(new DLBNode('^')); // set the terminating character for the word
                }
                else {
                    cur = cur.getDown();
                    if (cur.getCharacter() == '^') {
                        break;
                    }
                    else {
                        while (cur.getRight() != null && cur.getCharacter() != '^') {
                            cur = cur.getRight();
                        }
                        if (cur.getRight() == null && cur.getCharacter() != '^') {
                            cur.setRight(new DLBNode('^'));
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if the dictionary contains a given word.
     *
     * @param key The word to search for.
     * @return true if the word is found, false otherwise.
     */
    public boolean contains(String key) {
        if (key == null) { // handles null pointer exception
            return false;
        }
        else if (root == null) { // if the tree is empty return false
            return false;
        }

        // else we will traverse and check through traversion whether the key is in the tree
        DLBNode cur = root;
        //System.out.println(key.length());
        for (int i = 0; i < key.length(); i++) { // iterate through all of the characters in the key
            //System.out.print(i);
            //System.out.println();
            char c = key.charAt(i);

            while (cur != null && cur.getCharacter() != c) { // traverse the tier until we find the letter or null
                cur = cur.getRight();
            }

            if (cur == null) { // if we have found null, the character was not in the tier, thus the word not in the tree
                return false;
            }
            // else we found the character, traverse down to the next tier, increment the index, rinse repeat
            //System.out.println(cur.getLet());
            cur = cur.getDown();
        }

        while (cur != null) { // the final tier traversal, we must locate a '^'
            //System.out.println(cur.getLet());
            if (cur.getCharacter() == '^') {
                return true; // if we locate a '^', the word is in the tree, return true
            }
            cur = cur.getRight(); // traverse all the way to the end of the tier
        }

        return false; // if we didn't find a '^', it means it was only a prefix, not an entire word, return false
    }

    /**
     * Checks if a string is a valid prefix to any word in the dictionary.
     *
     * @param pre The prefix to search for.
     * @return true if the prefix is valid, false otherwise.
     */
    public boolean containsPrefix(String pre) {
        if (pre == null) { // handles null pointer exception
            return false;
        }
        else if (root == null) { // if the tree is empty return false
            return false;
        }

        // else we will traverse and check through traversion whether the key is in the tree
        DLBNode cur = root;
        for (int i = 0; i < pre.length(); i++) { // iterate through all of the characters in the key

            char c = pre.charAt(i);

            while (cur != null && cur.getCharacter() != c) { // traverse the tier until we find the letter or null
                cur = cur.getRight();
            }

            if (cur == null) { // if we have found null, the character was not in the tier, thus the word not in the tree
                return false;
            }

            // else we found the character, traverse down to the next tier, increment the index, rinse repeat
            cur = cur.getDown();
        }

        // if (cur.getLet() == '^' && cur.getRight() == null) {
            // return false;
        // }
        // if we have found each character in their appropriate tiers, return true
        return true;
    }

    /**
     * Performs a search one character at a time.
     *
     * @param next The next character in the search.
     * @return -1 if no valid word or prefix is found, 
     *          0 if a valid prefix but not a word,
     *          1 if a valid word but no prefix,
     *          2 if both a valid word and prefix.
     */
    public int searchByChar(char next) {
        if (cur == null) {
            cur = root; // start from the root if it's the first character
        }

        // traverse the tier until we find null or the char that we are looking for
        while (cur != null && cur.getCharacter() != next) {
            cur = cur.getRight();
        }

        // if we traversed to null, it means the char was not in the tier, no valid word or prefix found
        if (cur == null) {
            return -1; // no valid word or prefix found
        }

        suggestPrefix = suggestPrefix + cur.getCharacter(); // append the cur letter to the prefix for suggest()

        // else, we found the char in the tier, so we traverse down to check for '^' terminator, implying word, 
        // and we search for other letters in the tier implying prefix
        DLBNode terminator = cur.getDown();
        cur = terminator;

        boolean word = false; // boolean for word
        boolean prefix = false; // boolean for prefix

        while (terminator != null) { // traverse until null, or until we find the terminator
            char letter = terminator.getCharacter();
            if (letter == '^') {
                word = true; // it's a word
            }
            else if (!prefix) {
                prefix = true;
            }
            terminator = terminator.getRight();
        }

        if (word && prefix) {
            return 2;
        }
        else if (word) {
            return 1;
        }
        else if (prefix) {
            return 0;
        }
        else {
            cur = null;
            return -1;
        }
    }

    /**
     * Resets the by-character search.
     */
    public void resetByChar() {
        cur = null;
        suggestPrefix = "";
    }

    /**
     * Suggests up to 5 words from the dictionary based on the current
     * by-character search. Ordering should depend on the implementation.
     * 
     * @return ArrayList<String> List of up to 5 words that are prefixed by
     *         the current by-character search
     */
    public ArrayList<String> suggest() {
        // returns ArrayList in ascending order by their character value ("ASCIIbetical" order)
        ArrayList<String> suggestions = new ArrayList<>();
        suggest(cur, "", suggestions);
        suggestions.sort(null);
        ArrayList<String> suggestionsFinal = new ArrayList<>();
        for (int i = 0; i < Math.min(5, suggestions.size()); i++) {
            suggestionsFinal.add(suggestions.get(i));
        }
        return suggestionsFinal;
    }

    private void suggest(DLBNode cur, String word, ArrayList<String> suggestions) {
        // if (cur == null || suggestions.size() >= 5) {
        if (cur == null) {
            return;
        }
        if (cur.getCharacter() == '^') {
            suggestions.add(suggestPrefix + word);
        } else {
            suggest(cur.getDown(), word + cur.getCharacter(), suggestions);
        }
        suggest(cur.getRight(), word, suggestions);
    }

    /**
     * Suggests all words from the dictionary that match the current by-character search prefix.
     *
     * @return An ArrayList of all words prefixed by the current search input.
     */
    public ArrayList<String> suggestAll() {
        ArrayList<String> suggestions = new ArrayList<>();
        suggest(cur, "", suggestions);
        return suggestions;
    }
    
    /**
     * List all of the words currently stored in the dictionary
     * 
     * @return ArrayList<String> List of all valid words in the dictionary
     */
    public ArrayList<String> traverse() {
        ArrayList<String> wordList = new ArrayList<>();
        traverse(root, "", wordList);
        wordList.sort(null);
        return wordList;
    }

    /**
     * Recursive helper method to traverse the DLB and collect all valid words.
     *
     * @param cur The current DLBNode.
     * @param word The current word being built.
     * @param wordList The list to store all valid words.
     */
    private void traverse(DLBNode cur, String word, ArrayList<String> wordList) {
        if (cur == null) {
            return;
        }

        if (cur.getCharacter() == '^') {
            wordList.add(word);
        }
        else {
            traverse(cur.getDown(), word + cur.getCharacter(), wordList);
        }

        traverse(cur.getRight(), word, wordList);
    }

    /**
     * Count the number of words in the dictionary
     *
     * @return int, the number of (distinct) words in the dictionary
     */
    public int count() {
        return count(root);
    }

    /**
     * Recursive helper method to count words starting from a given node.
     *
     * @param node The current node to start counting from
     * @return int, the count of words under this node
     */
    private int count(DLBNode cur) {
        if (cur == null) { // base case
            return 0;
        }

        int count = 0;

        if (cur.getCharacter() == '^') {
            count = 1;
        }

        count += count(cur.getDown());
        count += count(cur.getRight());

        return count;
    }
    
}

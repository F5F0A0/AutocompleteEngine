package autocompleteengine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * The UserHistory class tracks a user's word history for the autocomplete engine.
 * 
 * It combines a De La Briandais (DLB) trie (prefix tree) for efficient prefix-based searching
 * with a HashMap to store and retrieve word frequencies quickly. This design
 * enables fast lookups, updates, and suggestions based on usage patterns.
 * 
 * Created by: Bridget Brinkman
 */
public class UserHistory {
    
    private DLB dlbHistory; // DLB trie to store words
    private HashMap<String, Integer> historyHashMap; // HashMap to store word frequencies

    /**
     * Constructs a new UserHistory object with an empty DLB-based dictionary
     * and a HashMap to store word frequencies.
     */
    public UserHistory() {
        dlbHistory = new DLB();
        historyHashMap = new HashMap<String, Integer>();
    }

    /**
     * Adds a word to the user's history or updates its frequency if it already exists.
     *
     * @param key The word to add or update.
     */
    public void add(String key) {
        dlbHistory.add(key);
        historyHashMap.put(key, historyHashMap.getOrDefault(key, 0) + 1);
    }

    /**
     * Adds a word with a specified frequency to the user's history.
     *
     * @param key   The word to add.
     * @param count The frequency count to assign or increment by.
     */
    public void add(String key, int count) {
        dlbHistory.add(key);
        historyHashMap.put(key, historyHashMap.getOrDefault(key, 0) + count);
    }

    /**
     * Checks if a specific word is present in the user's history.
     *
     * @param key The word to search for.
     * @return true if the word exists, false otherwise.
     */
    public boolean contains(String key) {
        return historyHashMap.containsKey(key);
    }

    /**
     * Determines if a given prefix exists in the user's word history.
     *
     * @param pre The prefix to check.
     * @return true if the prefix is found, false otherwise.
     */
    public boolean containsPrefix(String pre) {
        return dlbHistory.containsPrefix(pre);
    }

    /**
     * Searches the user's history one character at a time.
     *
     * @param next The next character to process.
     * @return -1 if no valid word or prefix exists,
     *          0 if a valid prefix but not a word,
     *          1 if a valid word but not a prefix,
     *          2 if both a valid word and prefix exist.
     */
    public int searchByChar(char next) {
        return dlbHistory.searchByChar(next);
    }

    /**
     * Resets the by-character search state.
     */
    public void resetByChar() {
        dlbHistory.resetByChar();
    }

    /**
     * Provides up to 5 suggestions based on the current search state.
     * 
     * Suggestions are ranked by frequency and then alphabetically.
     * 
     * @return A list of up to 5 suggested words matching the current search.
     */
    public ArrayList<String> suggest() {
        ArrayList<String> suggestions = dlbHistory.suggestAll();
    
        PriorityQueue<String> pq = new PriorityQueue<>((a, b) -> {
            int freqA = historyHashMap.getOrDefault(a, 0);
            int freqB = historyHashMap.getOrDefault(b, 0);
    
            if (freqA != freqB) {
                return Integer.compare(freqB, freqA);
            } 
            else {
                return a.compareTo(b);
            }
        });
    
        pq.addAll(suggestions);
    
        ArrayList<String> topSuggestions = new ArrayList<>();
        for (int i = 0; i < 5 && !pq.isEmpty(); i++) {
            topSuggestions.add(pq.poll());
        }
    
        return topSuggestions;
    }

    /**
     * Lists all words stored in the user's history.
     * 
     * @return ArrayList<String> List of all words in the user's history.
     */
    public ArrayList<String> traverse() {
        return new ArrayList<>(historyHashMap.keySet());
    }

    /**
     * Counts the total number of distinct words in the user's history.
     *
     * @return The number of unique words stored.
     */
    public int count() {
        return historyHashMap.size();
    }

    /**
     * Returns the HashMap storing the user's word history and their frequencies.
     * 
     * @return The HashMap of word frequencies.
     */
    public HashMap<String, Integer> getHistory() {
        return historyHashMap;
    }
}
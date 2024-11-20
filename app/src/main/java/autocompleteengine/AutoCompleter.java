package autocompleteengine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * The AutoCompleter class integrates a dictionary and a user history system 
 * to provide word autocomplete functionality. 
 * 
 * It uses a De La Briandais (DLB) trie (prefix tree) to store dictionary words for fast prefix-based 
 * lookups and a UserHistory component to track user-entered words and their frequencies. 
 * Suggestions are tailored to prioritize frequently used words.
 * 
 * Created by: Bridget Brinkman
 */
public class AutoCompleter {

    private DLB dictionary; // DLB structure to store dictionary words
    private UserHistory userHistory; // stores user input history (in a DLB trie) and word frequency (HashMap)

    /**
     * Constructs an AutoCompleter with both an English dictionary and user history files.
     * 
     * @param eng_dict_fname The file containing the dictionary words.
     * @param uhist_state_fname The file containing the user's word history.
     */
    public AutoCompleter(String eng_dict_fname, String uhist_state_fname) {
        dictionary = new DLB();
        userHistory = new UserHistory();

        loadDictionary(eng_dict_fname);
        loadUserHistory(uhist_state_fname);
    }

    /**
     * Constructs an AutoCompleter with only an English dictionary file.
     * 
     * @param eng_dict_fname The file containing the dictionary words.
     */
    public AutoCompleter(String eng_dict_fname) {
        dictionary = new DLB();
        userHistory = new UserHistory();

        loadDictionary(eng_dict_fname);
    }

    /**
     * Loads dictionary words from a file into the DLB trie.
     * Each word in the file is added to the dictionary.
     *
     * @param filename The file containing the dictionary words.
     */
    private void loadDictionary(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String word;
            while ((word = reader.readLine()) != null) {
                dictionary.add(word);
            }
        }
        catch (IOException e) {
            // System.out.println("Error loading dictionary from file: " + e.getMessage());
        }

    }

    /**
     * Loads the user's word history from a file into the UserHistory system.
     * The file should have words and their frequencies, separated by spaces.
     *
     * @param filename The file containing the user history.
     */
    private void loadUserHistory(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    String word = parts[0];
                    int count = Integer.parseInt(parts[1]);
                    userHistory.add(word, count);
                }
            }
        } catch (IOException e) {
            // System.out.println("No user history found, starting fresh.");
        }
    }
    
    /**
     * Processes the next character in the search query and updates autocomplete suggestions.
     * Suggestions are drawn from both the dictionary and user history, 
     * prioritizing user-entered words by frequency.
     *
     * @param next The next character in the search input.
     * @return A list of up to 5 suggested words based on the current search input.
     */
    public ArrayList<String> nextChar(char next) {
        dictionary.searchByChar(next);  // search in dictionary
        userHistory.searchByChar(next); // search in user history

        ArrayList<String> userSuggestions = userHistory.suggest();
        ArrayList<String> dictionarySuggestions = dictionary.suggest();

        ArrayList<String> finalSuggestions = new ArrayList<>();

        // add suggestions from user history first
        for (String suggestion : userSuggestions) {
            if (finalSuggestions.size() >= 5) break;
            finalSuggestions.add(suggestion);
        }

        // add suggestions from dictionary, avoiding duplicates
        for (String suggestion : dictionarySuggestions) {
            if (finalSuggestions.size() >= 5) break;
            if (!finalSuggestions.contains(suggestion)) {
                finalSuggestions.add(suggestion);
            }
        }

        return finalSuggestions;
    }

    /**
     * Finalizes the current search, updates the user's word history, 
     * and resets the autocomplete state for the next search.
     *
     * @param cur The word that was selected by the user.
     */
    public void finishWord(String cur) {
        resetByChar();
        userHistory.add(cur);
    }

    // saves the state of the UserHistory object to a file
    // saving and loading the user history should have an O(n) runtime (where n is the number of distinct words in the UserHistory object)
    
    /**
     * Saves the user's word history, including frequencies, to a file.
     * The file is written in the format: "word frequency" on each line.
     * 
     * @param filename The file to save the user's word history to.
     */
    public void saveUserHistory(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, Integer> entry : userHistory.getHistory().entrySet()) {
                writer.write(entry.getKey() + " " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            // System.out.println("Error saving user history: " + e.getMessage());
        }
    }

    /**
     * Resets the state of the by-character search for both the dictionary and user history.
     */
    private void resetByChar() {
        dictionary.resetByChar();
        userHistory.resetByChar();
    }
}
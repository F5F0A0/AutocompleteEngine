# Autocomplete Engine

This project implements a text prediction engine in Java, combining a **De La Briandais (DLB) trie (prefix tree)** for dictionary lookups with a **UserHistory** system for personalized suggestions based on word usage frequency.

---

## Features

- **Efficient Autocomplete**:
  - Provides up to 5 word suggestions based on user input.
  - Combines dictionary-based predictions with personalized user history.
  
- **Dynamic Learning**:
  - Tracks user-selected words and adapts future suggestions to prioritize frequently used terms.

- **File I/O**:
  - Loads an English dictionary and user history from files.
  - Saves user history for persistence between sessions.

- **Error Handling**:
  - Handles missing files and invalid inputs gracefully.

---

## Usage

The `AutoCompleter` class integrates the dictionary and user history systems, supporting the following operations:

### Example Usage
```java
// Create an AutoCompleter with a dictionary file and an optional user history file
AutoCompleter engine = new AutoCompleter("dictionary.txt", "userHistory.txt");

// Process user input character by character
engine.nextChar('a');
engine.nextChar('p');

// Get predictions
System.out.println(engine.nextChar('p')); // [apple, app, application, ...]

// Finalize a word
engine.finishWord("apple");

// Save user history
engine.saveUserHistory("userHistory.txt");
```

---

## Key Methods

### AutoCompleter
- `nextChar(char c)`: Updates suggestions based on user input.
- `finishWord(String word)`: Finalizes a word selection and updates the user history.
- `saveUserHistory(String filename)`: Saves the user history to a file.

### DLB
- `add(String key)`: Adds a word to the trie.
- `contains(String key)`: Checks if a word exists.
- `containsPrefix(String prefix)`: Checks if a prefix exists in the trie.
- `suggest()`: Returns suggestions for the current prefix.

### UserHistory
- Tracks word frequencies and integrates with the DLB for efficient retrieval.
- Suggests the most frequently selected words for any given prefix.

---

## Author

**Bridget Brinkman**  
GitHub: [@F5F0A0](https://github.com/F5F0A0)  
Project for **CS1501 - Algorithms and Data Structures II**, Fall 2024.
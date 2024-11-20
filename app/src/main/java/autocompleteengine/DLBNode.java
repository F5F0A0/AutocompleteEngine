package autocompleteengine;

import java.io.Serializable;

/**
 * DLBNode class for the Autocomplete Engine
 * 
 * Represents a node in a De La Briandais (DLB) trie structure, used to
 * efficiently store strings and retrieve them for autocomplete functionality.
 * 
 * Created by: Bridget Brinkman
 */

public class DLBNode implements Serializable {

    /**
     * Character stored at this node
     */
    private char character;

    /**
     * Pointer to the sibling node (alternative characters at this level)
     */
    private DLBNode right;

    /**
     * Leads to keys prefixed by the current path,
     * points to the child node (next character in the string)
     */
    private DLBNode down;

    /**
     * Constructor to initialize a node with a given character
     * 
     * @param character Character to be stored at this node
     */
    public DLBNode(char character) {
        this.character = character;
        this.right = null;
        this.down = null;
    }

    /**
     * Get the character stored at this node
     * 
     * @return Character at this node
     */
    public char getCharacter() {
        return character;
    }

    /**
     * Get the sibling of this node
     * 
     * @return Reference to the sibling node
     */
    public DLBNode getRight() {
        return right;
    }

    /**
     * Get the child of this node
     * 
     * @return Reference to the child node
     */
    public DLBNode getDown() {
        return down;
    }

    /**
     * Set the sibling for this node
     * 
     * @param sibling Node to set as the sibling
     */
    public void setRight(DLBNode r) {
        this.right = r;
    }

    /**
     * Set the child for this node
     * 
     * @param child Node to set as the child
     */
    public void setDown(DLBNode d) {
        this.down = d;
    }
}

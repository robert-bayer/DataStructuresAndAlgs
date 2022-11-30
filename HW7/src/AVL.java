import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Your implementation of an AVL.
 *
 * @author Robert
 * @version 1.0
 * @userid rbayer6
 * @GTID 903381275
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class AVL<T extends Comparable<? super T>> {

    // Do not add new instance variables or modify existing ones.
    private AVLNode<T> root;
    private int size;

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new AVL.
     *
     * This constructor should initialize the AVL with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * @param data the data to add to the tree
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("ERROR: please pass a valid collection");
        }
        for (T addData : data) {
            if (addData == null) {
                throw new IllegalArgumentException("ERROR: Data collection cannot contain null entries.");
            }
            add(addData);
        }

    }

    /**
     * Adds the element to the tree.
     *
     * Start by adding it as a leaf like in a regular BST and then rotate the
     * tree as necessary.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after adding the element, making sure to rebalance if
     * necessary.
     * 
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("ERROR: Data cannot equal null");
        }

        root = addHelper(root, data);
    }


    /**
     * Recursive helper for add function
     *
     * Comparing the current node and the data, this finds where the data
     * should be placed.
     *
     * Ignores if data is already in tree.
     *
     * @param curr The current node being compared against
     * @param data The desired data to be inserted
     * @return the current node for pointer reinforcement
     */
    private AVLNode<T> addHelper(AVLNode<T> curr, T data) {
        if (curr == null) {
            size++;
            return new AVLNode<T>(data);
        } else if (curr.getData().compareTo(data) < 0) {
            curr.setRight(addHelper(curr.getRight(), data));
        } else if (curr.getData().compareTo(data) > 0) {
            curr.setLeft(addHelper(curr.getLeft(), data));
        }
        return updateAndBalance(curr);
    }

    /**
     * Removes and returns the element from the tree matching the given
     * parameter.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the predecessor to
     * replace the data, NOT successor. As a reminder, rotations can occur
     * after removing the predecessor node.
     *
     * Remember to recalculate heights and balance factors while going back
     * up the tree after removing the element, making sure to rebalance if
     * necessary.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not found
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("ERROR: Data cannot equal null");
        }
        AVLNode<T> dummyNode = new AVLNode<>(null);
        root = removeHelp(root, data, dummyNode);
        return dummyNode.getData();
    }

    /**
     * Recursive helper method for the remove method
     *
     * Comparing the current node to the desired data to be deleted, when
     * the desired data is found, it is stored within a dummy node that
     * can be accessed by the main method.
     *
     * Has two ways of handling children depending on if it has 0, 1, or 2.
     * 0 or 1 it tells the parent of the deleted data to point at the deleted
     * data's child (which is null if it had no children).  If it had 2
     * children it finds the predecessor to the data, stores it in another
     * dummy node, and replaces the deleted data with the predecessor.
     *
     * @param curr The current node being compared against
     * @param data The desired data to be deleted
     * @param dummyNode The node that will store the deleted data
     * @return The node that the parent node should point at, both reinforcement
     *         and after the deletion.
     */
    private AVLNode<T> removeHelp(AVLNode<T> curr, T data, AVLNode<T> dummyNode) {
        if (curr == null) {
            throw new NoSuchElementException("ERROR: Tree does not contain specified data");
        } else if (curr.getData().compareTo(data) == 0) {

            dummyNode.setData(curr.getData());
            size--;

            if (curr.getRight() != null && curr.getLeft() != null) {     //If it has 2 children
                AVLNode<T> tempNode = new AVLNode<T>(null);
                curr.setLeft(findPredecessor(curr.getLeft(), tempNode));
                curr.setData(tempNode.getData());
            } else {     //If it has 0 or 1 child(ren)
                if (curr.getRight() == null) {
                    return curr.getLeft();
                } else {
                    return curr.getRight();
                }
            }
        } else if (curr.getData().compareTo(data) < 0) {
            curr.setRight(removeHelp(curr.getRight(), data, dummyNode));
        } else {
            curr.setLeft(removeHelp(curr.getLeft(), data, dummyNode));
        }
        return updateAndBalance(curr);
    }

    /**
     * Recursive helper to find the predecessor of a deleted node
     *
     * @param curr the node to find the predecessor to
     * @param tempNode a node to store the predecessor in.
     * @return tells the parent of the predecessor to point at the right
     * child (can be null)
     */
    private AVLNode<T> findPredecessor(AVLNode<T> curr, AVLNode<T> tempNode) {
        if (curr.getRight() == null) {
            tempNode.setData(curr.getData());
            return curr.getLeft();
        } else {
            curr.setRight(findPredecessor(curr.getRight(), tempNode));
        }
        return updateAndBalance(curr);
    }

    /**
     * Returns the element from the tree matching the given parameter.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * @param data the data to search for in the tree
     * @return the data in the tree equal to the parameter
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("ERROR: data cannot equal null");
        }
        return getHelp(root, data);
    }

    /**
     * Recursive helper to get method
     *
     * Compares the current to the desired data
     *
     * @param curr the current node being compared against
     * @param data the desired the data potentially within the tree
     * @return The data within the found node, error if not found
     */
    private T getHelp(AVLNode<T> curr, T data) {
        if (curr == null) {
            throw new NoSuchElementException("ERROR: " + data + " is not found within the tree");
        } else if (curr.getData().compareTo(data) == 0) {
            return curr.getData();
        } else if (curr.getData().compareTo(data) < 0) {
            return getHelp(curr.getRight(), data);
        } else {
            return getHelp(curr.getLeft(), data);
        }
    }

    /**
     * Returns whether or not data matching the given parameter is contained
     * within the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * @param data the data to search for in the tree.
     * @return true if the parameter is contained within the tree, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("ERROR: Data cannot equal null");
        }
        return containsHelp(root, data);
    }

    /**
     * Recursive helper for contains
     *
     * Compares the current node with the data to return a boolean
     * statement.
     * @param curr The current node being compared against
     * @param data The desired data potentially within the tree
     * @return A boolean value of whether data is within the tree or not
     */
    private boolean containsHelp(AVLNode<T> curr, T data) {
        if (curr == null) {
            return false;
        } else if (curr.getData().compareTo(data) == 0) {
            return true;
        } else if (curr.getData().compareTo(data) < 0) {
            return containsHelp(curr.getRight(), data);
        } else {
            return containsHelp(curr.getLeft(), data);
        }
    }

    /**
     * Returns the height of the root of the tree.
     * 
     * Should be O(1). 
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return heightHelp(root);
    }

    /**
     * Returns the height of a specified node
     *
     * @param node The node to find the height of
     * @return the height of said node
     */
    private int getHeight(AVLNode<T> node) {
        return heightHelp(node);
    }

    /**
     * Recursive helper to height
     *
     * It collects the height of the left subtree and right subtree and then compares
     * the returned values to see which it larger returning to the previously called
     * level.
     *
     * @param curr the root of the current subtree
     * @return the height of the current node, -1 if it is a null child.
     */
    private int heightHelp(AVLNode<T> curr) {
        if (curr == null) {
            return -1;
        }

        return curr.getHeight();
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     */
    public void clear() {
        size = 0;
        root = null;
    }

    /**
     * Returns the data in the deepest node. If there is more than one node
     * with the same deepest depth, return the rightmost (i.e. largest) node with 
     * the deepest depth. 
     * 
     * Should be recursive. 
     *
     * Must run in O(log n) for all cases.
     *
     * Example
     * Tree:
     *           2
     *        /    \
     *       0      3
     *        \
     *         1
     * Max Deepest Node:
     * 1 because it is the deepest node
     *
     * Example
     * Tree:
     *           2
     *        /    \
     *       0      4
     *        \    /
     *         1  3
     * Max Deepest Node:
     * 3 because it is the maximum deepest node (1 has the same depth but 3 > 1)
     *
     * @return the data in the maximum deepest node or null if the tree is empty
     */
    public T maxDeepestNode() {
        if (size == 0) {
            return null;
        }
        return maxDeepestNodeHelper(root);
    }

    /**
     * The helper method used to determine what the deepest Maximum node is
     *
     * @param node the current node
     * @return a recursive call to head towards the max node returning the max node
     *          once found
     */
    private T maxDeepestNodeHelper(AVLNode<T> node) {
        if (node.getHeight() == 0) {
            return node.getData();
        } else if (node.getBalanceFactor() <= 0) {
            return maxDeepestNodeHelper(node.getRight());
        } else {
            return maxDeepestNodeHelper(node.getLeft());
        }
    }

    /**
     * In BSTs, you learned about the concept of the successor: the
     * smallest data that is larger than the current data. However, you only
     * saw it in the context of the 2-child remove case.
     *
     * This method should retrieve (but not remove) the successor of the data
     * passed in. There are 2 cases to consider:
     * 1: The right subtree is non-empty. In this case, the successor is the
     * leftmost node of the right subtree.
     * 2: The right subtree is empty. In this case, the successor is the lowest
     * ancestor of the node whose left subtree contains the data. 
     * 
     * The second case means the successor node will be one of the node(s) we 
     * traversed left from to find data. Since the successor is the SMALLEST element 
     * greater than data, the successor node is the lowest/last node 
     * we traversed left from on the path to the data node.
     *
     * This should NOT be used in the remove method.
     * 
     * Should be recursive. 
     *
     * Ex:
     * Given the following AVL composed of Integers
     *                    76
     *                  /    \
     *                34      90
     *                  \    /
     *                  40  81
     * successor(76) should return 81
     * successor(81) should return 90
     * successor(40) should return 76
     *
     * @param data the data to find the successor of
     * @return the successor of data. If there is no larger data than the
     * one given, return null.
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T successor(T data) {
        if (data == null) {
            throw new IllegalArgumentException("ERROR: Data cannot be null");
        }
        AVLNode<T> dummyNode = new AVLNode<>(null);
        findSuccessor(root, data, dummyNode);
        return dummyNode.getData();
    }


    /**
     * Recursive helper to find the successor of a deleted node
     *
     * @param curr the node to find the successor to
     * @param data The data to find the successor of
     * @param dummyNode a node that will store the successor
     * @return tells the parent of the successor to point at the right
     * child (can be null)
     */
    private AVLNode<T> findSuccessor(AVLNode<T> curr, T data, AVLNode<T> dummyNode) {
        if (curr == null) {
            throw new NoSuchElementException("ERROR: desired data if not found within the tree");
        }
        if (curr.getData().compareTo(data) < 0) {
            return findSuccessor(curr.getRight(), data, dummyNode);
        } else if (curr.getData().compareTo(data) > 0) {
            dummyNode.setData(curr.getData());
            return findSuccessor(curr.getLeft(), data, dummyNode);
        } else {
            if (curr.getRight() != null) {
                findFoundSuccessor(curr.getRight(), dummyNode);
            }
        }
        return curr;
    }

    /**
     * A recursive call called if the found data has a child
     *
     * @param curr the current node
     * @param dummyNode the node storing the successor
     * @return the current node
     */
    private AVLNode<T> findFoundSuccessor(AVLNode<T> curr, AVLNode<T> dummyNode) {
        if (curr.getLeft() == null) {
            dummyNode.setData(curr.getData());
        } else {
            curr.setLeft(findFoundSuccessor(curr.getLeft(), dummyNode));
        }
        return curr;
    }

    /**
     * A method to rotate a subtree to the left and then update the heights and balance factors
     *
     * @param curr the current node being rotated
     * @return the new root
     */
    private AVLNode<T> leftRotate(AVLNode<T> curr) {
        AVLNode<T> temp = curr.getRight();
        curr.setRight(temp.getLeft());
        temp.setLeft(curr);
        updateHeightAndBF(curr);
        updateHeightAndBF(temp);
        return temp;
    }

    /**
     * A method that rotates the subtree to the right and then updates the heights and the balance factors
     * @param curr The current node being rotated
     * @return the new root
     */
    private AVLNode<T> rightRotate(AVLNode<T> curr) {
        AVLNode<T> temp = curr.getLeft();
        curr.setLeft(temp.getRight());
        temp.setRight(curr);
        updateHeightAndBF(curr);
        updateHeightAndBF(temp);
        return temp;
    }
    /**
     * Method that updates only the height and balance factor
     *
     * @param curr the current node being updated
     */
    private void updateHeightAndBF(AVLNode<T> curr) {
        int leftH = getHeight(curr.getLeft());
        int righthH = getHeight(curr.getRight());
        curr.setHeight(Math.max(leftH, righthH) + 1);
        curr.setBalanceFactor(leftH - righthH);
    }
    /**
     * A method that will ask the height and balance factor to be updated
     * It will then make any rotations as neccessary
     *
     * @param curr the current node being updated and rotated on
     * @return the new root
     */
    private AVLNode<T> updateAndBalance(AVLNode<T> curr) {
        updateHeightAndBF(curr);
        if (curr.getBalanceFactor() < -1) {
            if (curr.getRight().getBalanceFactor() > 0) {
                curr.setRight(rightRotate(curr.getRight()));
            }
            curr = leftRotate(curr);
        } else if (curr.getBalanceFactor() > 1) {
            if (curr.getLeft().getBalanceFactor() < 0) {
                curr.setLeft(leftRotate(curr.getLeft()));
            }
            curr = rightRotate(curr);
        }
        return curr;
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }

    /**
     * Returns the size of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}

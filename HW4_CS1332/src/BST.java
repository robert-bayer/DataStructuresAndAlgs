import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

/**
 * Your implementation of a BST.
 *
 * @author Robert Bayer
 * @version 1.0
 * @userid rbayer6
 * @GTID 903381275
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class BST<T extends Comparable<? super T>> {

    /*
     * Do not add new instance variables or modify existing ones.
     */
    private BSTNode<T> root;
    private int size;

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Constructs a new BST.
     *
     * This constructor should initialize the BST with the data in the
     * Collection. The data should be added in the same order it is in the
     * Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public BST(Collection<T> data) {
        for (T addData : data) {
            if (addData == null) {
                throw new IllegalArgumentException("ERROR: data cannot equal null");
            }
            add(addData);
        }
    }

    /**
     * Adds the data to the tree.
     *
     * This must be done recursively.
     *
     * The data becomes a leaf in the tree.
     *
     * Traverse the tree to find the appropriate location. If the data is
     * already in the tree, then nothing should be done (the duplicate
     * shouldn't get added, and size should not be incremented).
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("ERROR: Data cannot equal null");
        }

        root = addHelper(root, data);
        size += 1;
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
    private BSTNode<T> addHelper(BSTNode<T> curr, T data) {
        if (curr == null) {
            return new BSTNode<T>(data);
        } else if (curr.getData().compareTo(data) < 0) {
            curr.setRight(addHelper(curr.getRight(), data));
        } else if (curr.getData().compareTo(data) > 0) {
            curr.setLeft(addHelper(curr.getLeft(), data));
        }
        return curr;
    }

    /**
     * Removes and returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * There are 3 cases to consider:
     * 1: The node containing the data is a leaf (no children). In this case,
     * simply remove it.
     * 2: The node containing the data has one child. In this case, simply
     * replace it with its child.
     * 3: The node containing the data has 2 children. Use the successor to
     * replace the data. You MUST use recursion to find and remove the
     * successor (you will likely need an additional helper method to
     * handle this case efficiently).
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to remove
     * @return the data that was removed
     * @throws java.lang.IllegalArgumentException if data is null
     * @throws java.util.NoSuchElementException   if the data is not in the tree
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("ERROR: Data cannot equal null");
        }
        BSTNode<T> dummyNode = new BSTNode<>(null);
        root = removeHelp(root, data, dummyNode);
        size -= 1;
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
     * children it finds the successor to the data, stores it in another
     * dummy node, and replaces the deleted data with the successor.
     *
     * @param curr The current node being compared against
     * @param data The desired data to be deleted
     * @param dummyNode The node that will store the deleted data
     * @return The node that the parent node should point at, both reinforcement
     *         and after the deletion.
     */
    private BSTNode<T> removeHelp(BSTNode<T> curr, T data, BSTNode<T> dummyNode) {
        if (curr == null) {
            throw new NoSuchElementException("ERROR: Tree does not contain specified data");
        } else if (curr.getData().compareTo(data) == 0) {

            dummyNode.setData(curr.getData());

            if (curr.getRight() != null && curr.getLeft() != null) {     //If it has 2 children
                BSTNode<T> tempNode = new BSTNode<T>(null);
                curr.setRight(findSuccessor(curr.getRight(), tempNode));
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
        return curr;
    }

    /**
     * Recursive helper to find the successor of a deleted node
     *
     * @param curr the node to find the successor to
     * @param tempNode a node to store the successor in.
     * @return tells the parent of the successor to point at the right
     * child (can be null)
     */
    private BSTNode<T> findSuccessor(BSTNode<T> curr, BSTNode<T> tempNode) {
        if (curr.getLeft() == null) {
            tempNode.setData(curr.getData());
            return curr.getRight();
        } else {
            curr.setLeft(findSuccessor(curr.getLeft(), tempNode));
        }
        return curr;
    }

    /**
     * Returns the data from the tree matching the given parameter.
     *
     * This must be done recursively.
     *
     * Do not return the same data that was passed in. Return the data that
     * was stored in the tree.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
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
    private T getHelp(BSTNode<T> curr, T data) {
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
     * This must be done recursively.
     *
     * Hint: Should you use value equality or reference equality?
     *
     * Must be O(log n) for best and average cases and O(n) for worst case.
     *
     * @param data the data to search for
     * @return true if the parameter is contained within the tree, false
     *          otherwise
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
    private boolean containsHelp(BSTNode<T> curr, T data) {
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
     * Generate a pre-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the pre-order traversal of the tree
     */
    public List<T> preorder() {
        List<T> orderList = new ArrayList<>();
        return preoderHelp(root, orderList);
    }

    /**
     * Recursive helper to preorder
     *
     * Adds the root before handling the left and right subtree in that order
     *
     * @param root The root of the current subtree
     * @param orderList The list in which all of the data will be stored,
     *                  kept within the main method
     * @return The completed list of data in preorder
     */
    private List<T> preoderHelp(BSTNode<T> root, List<T> orderList) {
        if (root != null) {
            orderList.add(root.getData());
            preoderHelp(root.getLeft(), orderList);
            preoderHelp(root.getRight(), orderList);
        }
        return orderList;

    }


    /**
     * Generate an in-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the in-order traversal of the tree
     */
    public List<T> inorder() {
        List<T> orderList = new ArrayList<>();
        return inorderHelper(root, orderList);
    }

    /**
     * Recursive helper to inorder
     *
     * Handles the left subtree before adding the root and handling the right
     * subtree in that order
     *
     * @param root the root of the current subtree
     * @param orderList The list in which all of the data will be stored, kept
     *                  within the main method
     * @return The list of all data in the tree inorder
     */
    private List<T> inorderHelper(BSTNode<T> root, List<T> orderList) {
        if (root != null) {
            inorderHelper(root.getLeft(), orderList);
            orderList.add(root.getData());
            inorderHelper(root.getRight(), orderList);
        }
        return orderList;
    }

    /**
     * Generate a post-order traversal of the tree.
     *
     * This must be done recursively.
     *
     * Must be O(n).
     *
     * @return the post-order traversal of the tree
     */
    public List<T> postorder() {
        List<T> orderList = new ArrayList<>();
        return postorderHelper(root, orderList);
    }

    /**
     * Recursive helper to postorder
     *
     * Handles the left subtree and right subtree before adding the root in that order
     *
     * @param root the root of the current subtree
     * @param orderList The list in which all data will be stored kept within the main
     *                  method
     * @return The completed list with all of the data in postorder
     */
    private List<T> postorderHelper(BSTNode<T> root, List<T> orderList) {
        if (root != null) {
            postorderHelper(root.getLeft(), orderList);
            postorderHelper(root.getRight(), orderList);
            orderList.add(root.getData());
        }
        return orderList;
    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * This does not need to be done recursively.
     *
     * Hint: You will need to use a queue of nodes. Think about what initial
     * node you should add to the queue and what loop / loop conditions you
     * should use.
     *
     * Must be O(n).
     *
     * @return the level-order traversal of the tree
     */
    public List<T> levelorder() {
        BSTNode<T> curr;
        Queue<BSTNode<T>> q = new LinkedList<>();
        List<T> orderList = new ArrayList<>();
        q.add(root);
        while (!q.isEmpty()) {
            curr = q.remove();
            if (curr != null) {
                orderList.add(curr.getData());
                q.add(curr.getLeft());
                q.add(curr.getRight());
            }
        }
        return orderList;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * This must be done recursively.
     *
     * A node's height is defined as max(left.height, right.height) + 1. A
     * leaf node has a height of 0 and a null child has a height of -1.
     *
     * Must be O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return heightHelp(root);
    }

    /**
     * Recursive helper to height
     *
     * It collects the height of the left subtree and right subtree and then compares
     * the returned values to see which it larger returning to the previously called
     * level.
     *
     * @param root the root of the current subtree
     * @return the height of the current node, -1 if it is a null child.
     */
    private int heightHelp(BSTNode<T> root) {
        if (root == null) {
            return -1;
        }
        int heightl = heightHelp(root.getLeft());
        int heightr = heightHelp(root.getRight());

        return Math.max(heightl, heightr) + 1;
    }

    /**
     * Clears the tree.
     *
     * Clears all data and resets the size.
     *
     * Must be O(1).
     */
    public void clear() {
        size = 0;
        root = null;
    }

    /**
     * Generates a list of the max data per level from the top to the bottom
     * of the tree. (Another way to think about this is to get the right most
     * data per level from top to bottom.)
     * 
     * This must be done recursively.
     *
     * This list should contain the last node of each level.
     *
     * If the tree is empty, an empty list should be returned.
     *
     * Ex:
     * Given the following BST composed of Integers
     *      2
     *    /   \
     *   1     4
     *  /     / \
     * 0     3   5
     * getMaxDataPerLevel() should return the list [2, 4, 5] - 2 is the max
     * data of level 0, 4 is the max data of level 1, and 5 is the max data of
     * level 2
     *
     * Ex:
     * Given the following BST composed of Integers
     *               50
     *           /        \
     *         25         75
     *       /    \
     *      12    37
     *     /  \    \
     *   11   15   40
     *  /
     * 10
     * getMaxDataPerLevel() should return the list [50, 75, 37, 40, 10] - 50 is
     * the max data of level 0, 75 is the max data of level 1, 37 is the
     * max data of level 2, etc.
     *
     * Must be O(n).
     *
     * @return the list containing the max data of each level
     */
    public List<T> getMaxDataPerLevel() {
        int height = height();
        List<T> mplList = new LinkedList<>();
        for (int i = 0; i <= height; i++) {
            mplList = maxDataHelp(root, i, i, mplList);
        }
        return mplList;

    }

    /**
     * A recursive helper to getMaxDataPerLevel
     *
     * Iterates through each level a recursively calls itself to find the level it should
     * be looking at.
     *
     * @param root the root of the current subtree
     * @param depth the current depth it is looking at
     * @param level the depth that it is looking for
     * @param mplList the list of the max data per level
     * @return the list of data as it is being constructed and after completion.
     */
    private List<T> maxDataHelp(BSTNode<T> root, int depth, int level, List<T> mplList) {
        if (root == null) {
            return mplList;
        } else if (depth == 0) {
            if (mplList.size() == level) {
                mplList.add(level, root.getData());
            } else {
                mplList.set(level, root.getData());
            }
        } else {
            maxDataHelp(root.getLeft(), depth - 1, level, mplList);
            maxDataHelp(root.getRight(), depth - 1, level, mplList);
        }
        return mplList;
    }

    /**
     * Returns the root of the tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
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

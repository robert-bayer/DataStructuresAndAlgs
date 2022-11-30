import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Your implementation of a MinHeap.
 *
 * @author Robert Bayer
 * @version 1.0
 * @userid rbayer6
 * @GTID 903381275
 *
 * Collaborators: Tomer (TA)
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class MinHeap<T extends Comparable<? super T>> {

    /**
     * The initial capacity of the MinHeap when created with the default
     * constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    // Do not add new instance variables or modify existing ones.
    private T[] backingArray;
    private int size;

    /**
     * Constructs a new MinHeap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     * To initialize the backing array, create a Comparable array and then cast
     * it to a T array.
     */
    public MinHeap() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Creates a properly ordered heap from a set of initial values.
     *
     * You must use the BuildHeap algorithm that was taught in lecture! Simply
     * adding the data one by one using the add method will not get any credit.
     * As a reminder, this is the algorithm that involves building the heap
     * from the bottom up by repeated use of downHeap operations.
     *
     * Before doing the algorithm, first copy over the data from the
     * ArrayList to the backingArray (leaving index 0 of the backingArray
     * empty). The data in the backingArray should be in the same order as it
     * appears in the passed in ArrayList before you start the BuildHeap
     * algorithm.
     *
     * The backingArray should have capacity 2n + 1 where n is the
     * size of the passed in ArrayList (not INITIAL_CAPACITY). Index 0 should
     * remain empty, indices 1 to n should contain the data in proper order, and
     * the rest of the indices should be empty.
     *
     * @param data a list of data to initialize the heap with
     * @throws java.lang.IllegalArgumentException if data or any element in data
     *                                            is null
     */
    public MinHeap(ArrayList<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("ERROR: Passed data set must not be null.");
        }
        backingArray = (T[]) new Comparable[(2 * data.size()) + 1];
        size = 0;
        for (T term : data) {
            if (term == null) {
                throw new IllegalArgumentException("Error: Passed term within data set must not be null");
            }
            backingArray[size + 1] = term;
            size++;
        }
        for (int i = size / 2; i >= 1; i--) {
            downHeap(i);
        }
    }

    /**
     * Adds an item to the heap. If the backing array is full (except for
     * index 0) and you're trying to add a new item, then double its capacity.
     * The order property of the heap must be maintained after adding. You can
     * assume that no duplicate data will be passed in.
     * 
     * @param data the data to add
     * @throws java.lang.IllegalArgumentException if data is null
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("ERROR: Data must not be null");
        }
        if (size == backingArray.length - 1) {
            T[] temp = (T[]) new Comparable[(backingArray.length * 2)];
            for (int i = 1; i <= size; i++) {
                temp[i] = backingArray[i];
            }
            /*
            while (i <= size && backingArray[i] != null){
                temp[i] = backingArray [i];
                i ++;
            }
             */
            temp[size + 1] = data;
            size += 1;
            backingArray = temp;
            upHeap();
        } else if (size == 0) {
            backingArray[1] = data;
            size++;
        } else if (size > 0) {
            backingArray[size + 1] = data;
            size += 1;
            upHeap();
        }
    }

    /**
     * Removes and returns the min item of the heap. As usual for array-backed
     * structures, be sure to null out spots as you remove. Do not decrease the
     * capacity of the backing array.
     * The order property of the heap must be maintained after removing.
     *
     * @return the data that was removed
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T remove() {
        if (isEmpty()) {
            throw new NoSuchElementException("ERROR: Heap is Empty");
        }
        T returnData = backingArray[1];
        if (size == 1) {
            clear();
            return returnData;
        }

        returnData = backingArray[1];
        backingArray[1] = backingArray[size];
        backingArray[size] = null;
        size -= 1;
        downHeap(1);
        return returnData;
    }

    /**
     * Performs an upHeap on the most recently added node to the tree
     */
    private void upHeap() {
        int i = size;
        while (i > 1 && backingArray[i / 2].compareTo(backingArray[i]) > 0) {
            T temp = backingArray[i];
            backingArray[i] = backingArray[i / 2];
            backingArray[i / 2] = temp;
            i = i / 2;
        }
    }


    /**
     * Performs a downheap on the given node and works its way
     * all the way down
     *
     * This is done interatively
     * @param i the index where to start the downheap
     */
    private void downHeap(int i) {
        /*
        if (size == 2){
            if (backingArray[1].compareTo(backingArray[2]) > 0){
                T temp = backingArray[1];
                backingArray[1] = backingArray[2];
                backingArray[2] = temp;
            }
        }

         */

        while ((i * 2 <= size)) {
            if (((i * 2) + 1) <= size && backingArray[2 * i].compareTo(backingArray[(2 * i) + 1]) > 0) {
                if (backingArray[i].compareTo(backingArray[(2 * i) + 1]) > 0) {
                    T temp = backingArray[i];
                    backingArray[i] = backingArray[(2 * i) + 1];
                    backingArray[(2 * i) + 1] = temp;
                    i = (i * 2) + 1;
                } else {
                    break;
                }
            } else if (((i * 2) + 1) <= size && backingArray[2 * i].compareTo(backingArray[(2 * i) + 1]) <= 0) {
                if (backingArray[i].compareTo(backingArray[2 * i]) > 0) {
                    T temp = backingArray[i];
                    backingArray[i] = backingArray[2 * i];
                    backingArray[2 * i] = temp;
                    i = i * 2;
                } else {
                    break;
                }
            } else {
                if (backingArray[i].compareTo(backingArray[2 * i]) > 0) {
                    T temp = backingArray[i];
                    backingArray[i] = backingArray[2 * i];
                    backingArray[2 * i] = temp;
                    i = i * 2;
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Returns the minimum element in the heap.
     *
     * @return the minimum element
     * @throws java.util.NoSuchElementException if the heap is empty
     */
    public T getMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("ERROR: Heap is empty");
        }
        return backingArray[1];
    }

    /**
     * Returns whether or not the heap is empty.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the heap.
     *
     * Resets the backing array to a new array of the initial capacity and
     * resets the size.
     */
    public void clear() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the backing array of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the backing array of the list
     */
    public T[] getBackingArray() {
        // DO NOT MODIFY THIS METHOD!
        return backingArray;
    }

    /**
     * Returns the size of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }
}

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Your implementation of a LinearProbingHashMap.
 *
 * @author Robert Bayer
 * @version 1.0
 * @userid rbayer6
 * @GTID 903381275
 *
 * Collaborators: 1332 TAs
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class LinearProbingHashMap<K, V> {

    /**
     * The initial capacity of the LinearProbingHashMap when created with the
     * default constructor.
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final int INITIAL_CAPACITY = 13;

    /**
     * The max load factor of the LinearProbingHashMap
     *
     * DO NOT MODIFY THIS VARIABLE!
     */
    public static final double MAX_LOAD_FACTOR = 0.67;

    // Do not add new instance variables or modify existing ones.
    private LinearProbingMapEntry<K, V>[] table;
    private int size;

    /**
     * Constructs a new LinearProbingHashMap.
     *
     * The backing array should have an initial capacity of INITIAL_CAPACITY.
     *
     * Use constructor chaining.
     */
    public LinearProbingHashMap() {
        this(INITIAL_CAPACITY);                         //Calls the below function to create a table of INITIAL_CAPACITY

    }

    /**
     * Constructs a new LinearProbingHashMap.
     *
     * The backing array should have an initial capacity of initialCapacity.
     *
     * You may assume initialCapacity will always be positive.
     *
     * @param initialCapacity the initial capacity of the backing array
     */
    public LinearProbingHashMap(int initialCapacity) {
        table = (LinearProbingMapEntry[]) new LinearProbingMapEntry[initialCapacity];
        size = 0;

    }

    /**
     * Adds the given key-value pair to the map. If an entry in the map
     * already has this key, replace the entry's value with the new one
     * passed in.
     *
     * In the case of a collision, use linear probing as your resolution
     * strategy.
     *
     * Before actually adding any data to the HashMap, you should check to
     * see if the array would violate the max load factor if the data was
     * added. For example, let's say the array is of length 5 and the current
     * size is 3 (LF = 0.6). For this example, assume that no elements are
     * removed in between steps. If another entry is attempted to be added,
     * before doing anything else, you should check whether (3 + 1) / 5 = 0.8
     * is larger than the max LF. It is, so you would trigger a resize before
     * you even attempt to add the data or figure out if it's a duplicate. Be
     * careful to consider the differences between integer and double
     * division when calculating load factor.
     *
     * When regrowing, resize the length of the backing table to
     * 2 * old length + 1. You must use the resizeBackingTable method to do so.
     *
     * Return null if the key was not already in the map. If it was in the map,
     * return the old value associated with it.
     *
     * @param key   the key to add
     * @param value the value to add
     * @return null if the key was not already in the map. If it was in the
     * map, return the old value associated with it
     * @throws java.lang.IllegalArgumentException if key or value is null
     */
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("ERROR: Key and Value cannot equal null");
        }

        if (!testLoad()) {                                           //If after adding, the load factor would exceed the
            resizeBackingTable((2 * table.length) + 1);       //MAX_LOAD_FACTOR, resize the array first.
        }

        int index = Math.abs(key.hashCode() % table.length);        //Setting the index of the <K, V> Pair
        int delIndex = -1;
        int counter = 0;
        V returnData;

        /*
        While loop stops if:
        1. The index is empty
        2. The index contains a key that is not removed
         */
        while (table[index] != null && counter != size) {
            if (table[index].getKey() == key && !table[index].isRemoved()) {
                returnData = table[index].getValue();
                table[index].setValue(value);
                return returnData;
            }
            if (!table[index].isRemoved()) {
                counter++;
            }

            if (table[index].isRemoved() && delIndex == -1) {
                delIndex = index;            //Tests if delIndex has been changed before, it will only be -1 by default.
            }  //Sets it to current index if the current index is marked DEL and if delIndex is still its default value.

            index = (index + 1) % table.length;       //Increment index
        }


        if (delIndex != -1) {             //If index is not its default value, set the index to where the first DEL was.
            index = delIndex;
        }

        //Create a new entry to override any data in the current index.
        LinearProbingMapEntry insertData = new LinearProbingMapEntry(key, value);

        returnData = null;
        table[index] = insertData;
        size++;

        return returnData;                //Return the value.

    }

    /**
     * Removes the entry with a matching key from map by marking the entry as
     * removed.
     *
     * @param key the key to remove
     * @return the value previously associated with the key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("ERROR: Key cannot equal null");
        }

        int index = Math.abs(key.hashCode() % table.length);
        int counter = 0;

        while (table[index] != null && counter != size) {
            if (table[index].getKey() == key && !table[index].isRemoved()) {
                table[index].setRemoved(true);
                size--;
                return table[index].getValue();

            }
            if (!table[index].isRemoved()) {
                counter++;
            }
            index = (index + 1) % table.length;
        }

        throw new NoSuchElementException("Error: Key is not within the hashmap");

    }

    /**
     * Gets the value associated with the given key.
     *
     * @param key the key to search for in the map
     * @return the value associated with the given key
     * @throws java.lang.IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException   if the key is not in the map
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("ERROR: key cannot be null");
        }

        int index = Math.abs(key.hashCode() % table.length);
        int counter = 0;

        while (table[index] != null && counter != size) {
            if (table[index].getKey() == key && !table[index].isRemoved()) {
                return table[index].getValue();
            }
            if (!table[index].isRemoved()) {
                counter++;
            }
            index = (index + 1) % table.length;
        }

        throw new NoSuchElementException("ERROR: Key not within hashmap");

    }

    /**
     * Returns whether or not the key is in the map.
     *
     * @param key the key to search for in the map
     * @return true if the key is contained within the map, false
     * otherwise
     * @throws java.lang.IllegalArgumentException if key is null
     */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("ERROR: Key cannot be null");
        }

        int index = Math.abs(key.hashCode() % table.length);
        int counter = 0;

        while (table[index] != null && counter != size) {
            if (table[index].getKey() == key && !table[index].isRemoved()) {
                return true;
            }
            if (!table[index].isRemoved()) {
                counter++;
            }
            index = (index + 1) % table.length;
        }

        return false;


    }

    /**
     * Returns a Set view of the keys contained in this map.
     *
     * Use java.util.HashSet.
     *
     * @return the set of keys in this map
     */
    public Set<K> keySet() {

        Set<K> hmKeys = new HashSet<>(size);

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isRemoved()) {
                hmKeys.add(table[i].getKey());
            }
        }
        return hmKeys;
    }

    /**
     * Returns a List view of the values contained in this map.
     *
     * Use java.util.ArrayList or java.util.LinkedList.
     *
     * You should iterate over the table in order of increasing index and add
     * entries to the List in the order in which they are traversed.
     *
     * @return list of values in this map
     */
    public List<V> values() {

        List<V> hmValues = new ArrayList<>(size);

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isRemoved()) {
                hmValues.add(table[i].getValue());
            }
        }
        return hmValues;

    }

    /**
     * Resize the backing table to length.
     *
     * Disregard the load factor for this method. So, if the passed in length is
     * smaller than the current capacity, and this new length causes the table's
     * load factor to exceed MAX_LOAD_FACTOR, you should still resize the table
     * to the specified length and leave it at that capacity.
     *
     * You should iterate over the old table in order of increasing index and
     * add entries to the new table in the order in which they are traversed. 
     * You should NOT copy over removed elements to the resized backing table.
     *
     * Since resizing the backing table is working with the non-duplicate
     * data already in the table, you shouldn't explicitly check for
     * duplicates.
     *
     * Hint: You cannot just simply copy the entries over to the new array.
     *
     * @param length new length of the backing table
     * @throws java.lang.IllegalArgumentException if length is less than the
     *                                            number of items in the hash
     *                                            map
     */
    public void resizeBackingTable(int length) {
        if (length < size) {
            throw new IllegalArgumentException("ERROR: Length must be larger than the current number of items in map");
        }


        LinearProbingMapEntry<K, V>[] temp = (LinearProbingMapEntry[]) new LinearProbingMapEntry[length];

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null && !table[i].isRemoved()) {
                int index = Math.abs((table[i].getKey().hashCode()) % temp.length);
                while (temp[index] != null) {
                    index = (index + 1) % temp.length;
                }
                LinearProbingMapEntry insertData = new LinearProbingMapEntry(table[i].getKey(), table[i].getValue());
                temp[index] = insertData;
            }
        }
        table = temp;
    }

    /**
     * Clears the map.
     *
     * Resets the table to a new array of the INITIAL_CAPACITY and resets the
     * size.
     *
     * Must be O(1).
     */
    public void clear() {

        table = (LinearProbingMapEntry[]) new LinearProbingMapEntry[INITIAL_CAPACITY];
        size = 0;

    }

    /**
     * Returns the table of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the table of the map
     */
    public LinearProbingMapEntry<K, V>[] getTable() {
        // DO NOT MODIFY THIS METHOD!
        return table;
    }

    /**
     * Returns the size of the map.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the map
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }

    /**
     * Calculates the Load Factor of the backing array if another term were
     * to be added.  Returns true if another term is able to be added without
     * putting the backing array over the load factor.
     *
     * @return true if the load factor is valid with another term, false if it
     * is not
     */
    private boolean testLoad() {
        double loadFactor = (size + 1.0) / table.length;
        return loadFactor < MAX_LOAD_FACTOR;
    }
}

import java.util.Comparator;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

/**
 * Your implementation of various sorting algorithms.
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
public class Sorting {

    /**
     * Implement insertion sort.
     *
     * It should be:
     * in-place
     * stable
     * adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n)
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void insertionSort(T[] arr, Comparator<T> comparator) {

        if (arr == null || comparator == null) {
            throw new IllegalArgumentException("ERROR: Array or Comparator cannot be null");
        }

        int i = 0;
        while (i < arr.length) {
            int j = i;
            T tempVar;
            while (j - 1 >= 0 && comparator.compare(arr[j], arr[j - 1]) < 0) {
                tempVar = arr[j];
                arr[j] = arr[j - 1];
                arr[j - 1] = tempVar;
                j--;
            }
            i++;
        }

    }

    /**
     * Implement cocktail sort.
     *
     * It should be:
     * in-place
     * stable
     * adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n)
     *
     * NOTE: See pdf for last swapped optimization for cocktail sort. You
     * MUST implement cocktail sort with this optimization
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void cocktailSort(T[] arr, Comparator<T> comparator) {

        if (arr == null || comparator == null) {
            throw new IllegalArgumentException("ERROR: Array or Comparator cannot be null");
        }

        int frontMark = -1;
        int backMark = arr.length;
        int lastSwapped = -1;

        while (backMark - frontMark > 2) {

            lastSwapped = bubbleBack(arr, comparator, frontMark, backMark);

            if (lastSwapped == frontMark) {
                break;
            } else {
                backMark = lastSwapped;
            }

            lastSwapped = bubbleFront(arr, comparator, frontMark, backMark);

            if (lastSwapped == backMark) {
                break;
            } else {
                frontMark = lastSwapped;
            }

        }




    }

    /**
     * A helper method which bubbles any data larger than its neighbor to
     * the right backwards
     *
     * @param arr The array getting sorted
     * @param comparator The comparator passed
     * @param frontMark The front of the unsorted section of the array
     * @param backMark The back of the unsorted section of the array
     * @return The index of the last swap made
     * @param <T> Data type to sort
     */
    private static <T> int bubbleBack(T[] arr, Comparator<T> comparator, int frontMark, int backMark) {
        int i = frontMark + 1;
        T tempData;
        int lastSwap = frontMark;

        while (i < backMark - 1) {
            if (comparator.compare(arr[i], arr[i + 1]) > 0) {
                tempData = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = tempData;
                lastSwap = i + 1;
            }
            i++;
        }
        return lastSwap;
    }

    /**
     * A helper method which bubbles any data smaller than its neighbor to
     * the left forwards
     *
     * @param arr The array getting sorted
     * @param comparator The comparator passed
     * @param frontMark The front of the unsorted section of the array
     * @param backMark The back of the unsorted section of the array
     * @return The index of the last swap made
     * @param <T> Data type to sort
     */
    private static <T> int bubbleFront(T[] arr, Comparator<T> comparator, int frontMark, int backMark) {
        int i = backMark - 1;
        T tempData;
        int lastSwap = frontMark;

        while (i > frontMark + 1) {
            if (comparator.compare(arr[i], arr[i - 1]) < 0) {
                tempData = arr[i];
                arr[i] = arr[i - 1];
                arr[i - 1] = tempData;
                lastSwap = i - 1;
            }
            i--;
        }
        return lastSwap;
    }

    /**
     * Implement merge sort.
     *
     * It should be:
     * out-of-place
     * stable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n log n)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * You can create more arrays to run merge sort, but at the end, everything
     * should be merged back into the original T[] which was passed in.
     *
     * When splitting the array, if there is an odd number of elements, put the
     * extra data on the right side.
     *
     * Hint: If two data are equal when merging, think about which subarray
     * you should pull from first
     *
     * @param <T>        data type to sort
     * @param arr        the array to be sorted
     * @param comparator the Comparator used to compare the data in arr
     * @throws java.lang.IllegalArgumentException if the array or comparator is
     *                                            null
     */
    public static <T> void mergeSort(T[] arr, Comparator<T> comparator) {

        if (arr == null || comparator == null) {
            throw new IllegalArgumentException("ERROR: Array or Comparator cannot be null");
        }

        if (arr.length > 1) {
            int length = arr.length;
            int mid = length / 2;
            T[] leftArray = (T[]) new Object[mid];
            T[] rightArray = (T[]) new Object[length - leftArray.length];
            for (int i = 0; i < leftArray.length; i++) {
                leftArray[i] = arr[i];
            }
            for (int i = 0; i < rightArray.length; i++) {
                rightArray[i] = arr[i + leftArray.length];
            }


            mergeSort(leftArray, comparator);
            mergeSort(rightArray, comparator);


            int left = 0;
            int right = 0;
            int curr = 0;

            while (left < mid && right < length - mid) {
                if (comparator.compare(leftArray[left], rightArray[right]) <= 0) {
                    arr[curr] = leftArray[left];
                    left++;
                } else {
                    arr[curr] = rightArray[right];
                    right++;
                }
                curr++;
            }
            while (left < mid) {
                arr[curr] = leftArray[left];
                curr++;
                left++;
            }
            while (right < length - mid) {
                arr[curr] = rightArray[right];
                curr++;
                right++;
            }
        }
    }

    /**
     * Implement quick sort.
     *
     * Use the provided random object to select your pivots. For example if you
     * need a pivot between a (inclusive) and b (exclusive) where b > a, use
     * the following code:
     *
     * int pivotIndex = rand.nextInt(b - a) + a;
     *
     * If your recursion uses an inclusive b instead of an exclusive one,
     * the formula changes by adding 1 to the nextInt() call:
     *
     * int pivotIndex = rand.nextInt(b - a + 1) + a;
     *
     * It should be:
     * in-place
     * unstable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n^2)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not receive
     * credit if you do not use the one we have taught you!
     *
     * @param <T>        data type to sort
     * @param arr        the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @param rand       the Random object used to select pivots
     * @throws java.lang.IllegalArgumentException if the array or comparator or
     *                                            rand is null
     */
    public static <T> void quickSort(T[] arr, Comparator<T> comparator, Random rand) {

        if (arr == null || comparator == null || rand == null) {
            throw new IllegalArgumentException("ERROR: Array or Comparator cannot be null");
        }

        quickSortHelper(arr, comparator, rand, 0, arr.length - 1);

    }

    /**
     * A helper method to do the main work of the quick sort method
     * Takes the array, the comparator, and the random generator, as
     * well as the beginning and end of the subarray to run the sort on.
     *
     * @param arr The array being sorted
     * @param comparator The comparator
     * @param rand The Random number generator
     * @param start The starting index of the sub array being sorted
     * @param end The ending index of the sub array being sorted
     * @param <T> The data type being sorted.
     */
    public static <T> void quickSortHelper(T[] arr, Comparator<T> comparator, Random rand, int start, int end) {

        if (end <= start) {
            return;
        }
        int pIndex = rand.nextInt(end - start + 1) + start;
        T pData = arr[pIndex];
        arr[pIndex] = arr[start];
        arr[start] = pData;
        int i = start + 1;
        int j = end;
        while (j >= i) {
            while (j >= i && comparator.compare(arr[i], pData) <= 0) {
                i++;
            }
            while (j >= i && comparator.compare(arr[j], pData) >= 0) {
                j--;
            }
            if (j >= i) {
                T tempData = arr[i];
                arr[i] = arr[j];
                arr[j] = tempData;
                i++;
                j--;
            }
        }

        T tempData = arr[j];
        arr[j] = pData;
        arr[start] = tempData;
        quickSortHelper(arr, comparator, rand, start, j - 1);
        quickSortHelper(arr, comparator, rand, j + 1, end);
    }

    /**
     * Implement LSD (least significant digit) radix sort.
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not get full
     * credit if you do not implement the one we have taught you!
     *
     * Remember you CANNOT convert the ints to strings at any point in your
     * code! Doing so may result in a 0 for the implementation.
     *
     * It should be:
     * out-of-place
     * stable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(kn)
     *
     * And a best case running time of:
     * O(kn)
     *
     * You are allowed to make an initial O(n) passthrough of the array to
     * determine the number of iterations you need. The number of iterations
     * can be determined using the number with the largest magnitude.
     *
     * At no point should you find yourself needing a way to exponentiate a
     * number; any such method would be non-O(1). Think about how how you can
     * get each power of BASE naturally and efficiently as the algorithm
     * progresses through each digit.
     *
     * Refer to the PDF for more information on LSD Radix Sort.
     *
     * You may use ArrayList or LinkedList if you wish, but it may only be
     * used inside radix sort and any radix sort helpers. Do NOT use these
     * classes with other sorts. However, be sure the List implementation you
     * choose allows for stability while being as efficient as possible.
     *
     * Do NOT use anything from the Math class except Math.abs().
     *
     * @param arr the array to be sorted
     * @throws java.lang.IllegalArgumentException if the array is null
     */
    public static void lsdRadixSort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("ERROR: Array or Comparator cannot be null");
        }

        LinkedList<Integer>[] buckets = new LinkedList[19];

        for (int i = 0; i <= 18; i++) {
            buckets[i] = new LinkedList<Integer>();
        }

        int k = 0;

        for (int data : arr) {
            if (data < MAX_VALUE && data > MIN_VALUE) {
                if (Math.abs(data) < 100000) {
                    if (Math.abs(data) < 100) {
                        if (Math.abs(data) < 10) {
                            if (k < 1) {
                                k = 1;
                            }
                        } else {
                            if (k < 2) {
                                k = 2;
                            }
                        }
                    } else {
                        if (Math.abs(data) < 1000) {
                            if (k < 3) {
                                k = 3;
                            }
                        } else {
                            if (Math.abs(data) < 10000) {
                                if (k < 4) {
                                    k = 4;
                                }
                            } else {
                                if (k < 5) {
                                    k = 5;
                                }
                            }
                        }
                    }
                } else {
                    if (Math.abs(data) < 10000000) {
                        if (Math.abs(data) < 1000000) {
                            if (k < 6) {
                                k = 6;
                            }
                        } else {
                            if (k < 7) {
                                k = 7;
                            }
                        }
                    } else {
                        if (Math.abs(data) < 100000000) {
                            if (k < 8) {
                                k = 8;
                            }
                        } else {
                            if (Math.abs(data) < 1000000000) {
                                if (k < 9) {
                                    k = 9;
                                }
                            } else {
                                if (k < 10) {
                                    k = 10;
                                }
                            }
                        }
                    }
                }
            } else if (data > MAX_VALUE || data < MIN_VALUE) {
                throw new IllegalArgumentException("ERROR: the data you passed is too large or too small to be parsed");
            } else if (data == MAX_VALUE || data == MIN_VALUE) {
                k = 10;
            }
        }

        int exponentialTen = 1;
        for (int i = 1; i <= k; i++) {
            for (int data : arr) {
                int digit = (data / exponentialTen) % 10;
                buckets[digit + 9].add(data);
            }

            int tempPointer = 0;
            for (LinkedList<Integer> bucket : buckets) {
                while (!bucket.isEmpty()) {
                    arr[tempPointer] = bucket.removeFirst();
                    tempPointer++;
                }
            }
            exponentialTen = exponentialTen * 10;
        }

    }

    /**
     * Implement heap sort.
     *
     * It should be:
     * out-of-place
     * unstable
     * not adaptive
     *
     * Have a worst case running time of:
     * O(n log n)
     *
     * And a best case running time of:
     * O(n log n)
     *
     * Use java.util.PriorityQueue as the heap. Note that in this
     * PriorityQueue implementation, elements are removed from smallest
     * element to largest element.
     *
     * Initialize the PriorityQueue using its build heap constructor (look at
     * the different constructors of java.util.PriorityQueue).
     *
     * Return an int array with a capacity equal to the size of the list. The
     * returned array should have the elements in the list in sorted order.
     *
     * @param data the data to sort
     * @return the array with length equal to the size of the input list that
     * holds the elements from the list is sorted order
     * @throws java.lang.IllegalArgumentException if the data is null
     */
    public static int[] heapSort(List<Integer> data) {
        if (data == null) {
            throw new IllegalArgumentException("ERROR: Array or Comparator cannot be null");
        }

        PriorityQueue<Integer> intSorter = new PriorityQueue<Integer>(data);
        int[] sortedArray = new int[data.size()];

        int i = 0;
        while (intSorter.peek() != null) {
            sortedArray[i] = intSorter.poll();
            i++;
        }
        return sortedArray;
    }
}

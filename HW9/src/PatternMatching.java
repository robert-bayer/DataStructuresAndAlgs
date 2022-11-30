import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Your implementations of various string searching algorithms.
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
public class PatternMatching {

    /**
     * Knuth-Morris-Pratt (KMP) algorithm relies on the failure table (also
     * called failure function). Works better with small alphabets.
     *
     * Make sure to implement the buildFailureTable() method before implementing
     * this method.
     *
     * @param pattern    the pattern you are searching for in a body of text
     * @param text       the body of text where you search for pattern
     * @param comparator you MUST use this to check if characters are equal
     * @return list containing the starting index for each match found
     * @throws java.lang.IllegalArgumentException if the pattern is null or has
     *                                            length 0
     * @throws java.lang.IllegalArgumentException if text or comparator is null
     */
    public static List<Integer> kmp(CharSequence pattern, CharSequence text, CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("ERROR: A pattern must be provided");
        }
        if (text == null || comparator == null) {
            throw new IllegalArgumentException("ERROR: A text and/or comparator must be provided");
        }

        int n = text.length();
        int m = pattern.length();
        ArrayList<Integer> occurrences = new ArrayList<Integer>();
        if (m > n) {
            return occurrences;
        }

        int i = 0;
        int j = 0;
        int[] failureTable = buildFailureTable(pattern, comparator);
        while (i <= n - m) {
            while (j < m && comparator.compare(text.charAt(i + j), pattern.charAt(j)) == 0) {
                j++;
            }
            if (j == 0) {
                i++;
            } else {
                if (j == m) {
                    occurrences.add(i);
                }
                i = i + j - failureTable[j - 1];
                j = failureTable[j - 1];
            }
        }

        /*
        int i = 0;
        int j = 0;
        int[] failureTable = buildFailureTable(pattern, comparator);
        while (i <= n - m) {
            if (comparator.compare(text.charAt(i + j), pattern.charAt(j)) == 0) {
                if (j == m - 1) {
                    occurrences.add(i);
                    i++;
                    j = failureTable[j];
                } else {
                    j++;
                }
            } else {
                if (j == 0) {
                    i++;
                } else {
                    i = i + j - failureTable[j - 1];
                    j = failureTable[j - 1];
                }
            }
        }

         */
        return occurrences;
    }

    /**
     * Builds failure table that will be used to run the Knuth-Morris-Pratt
     * (KMP) algorithm.
     *
     * The table built should be the length of the input pattern.
     *
     * Note that a given index i will contain the length of the largest prefix
     * of the pattern indices [0..i] that is also a suffix of the pattern
     * indices [1..i]. This means that index 0 of the returned table will always
     * be equal to 0
     *
     * Ex. pattern = ababac
     *
     * table[0] = 0
     * table[1] = 0
     * table[2] = 1
     * table[3] = 2
     * table[4] = 3
     * table[5] = 0
     *
     * If the pattern is empty, return an empty array.
     *
     * @param pattern    a pattern you're building a failure table for
     * @param comparator you MUST use this to check if characters are equal
     * @return integer array holding your failure table
     * @throws java.lang.IllegalArgumentException if the pattern or comparator
     *                                            is null
     */
    public static int[] buildFailureTable(CharSequence pattern, CharacterComparator comparator) {
        if (pattern == null || comparator == null) {
            throw new IllegalArgumentException("ERROR: Pattern nor Comparator can equal null");
        }

        int m = pattern.length();
        int[] failureTable = new int[m];
        if (m == 0) {
            return failureTable;
        }
        int i = 0;
        int j = 1;
        failureTable[0] = 0;
        while (j < m) {
            if (comparator.compare(pattern.charAt(i), pattern.charAt(j)) == 0) {
                failureTable[j] = i + 1;
                i++;
                j++;
            } else if (i == 0) {
                failureTable[j] = 0;
                j++;
            } else {
                i = failureTable[i - 1];
            }
        }

        return failureTable;
    }

    /**
     * Boyer Moore algorithm that relies on last occurrence table. Works better
     * with large alphabets.
     *
     * Make sure to implement the buildLastTable() method before implementing
     * this method. Do NOT implement the Galil Rule in this method.
     *
     * Note: You may find the getOrDefault() method from Java's Map class
     * useful.
     *
     * @param pattern    the pattern you are searching for in a body of text
     * @param text       the body of text where you search for the pattern
     * @param comparator you MUST use this to check if characters are equal
     * @return list containing the starting index for each match found
     * @throws java.lang.IllegalArgumentException if the pattern is null or has
     *                                            length 0
     * @throws java.lang.IllegalArgumentException if text or comparator is null
     */
    public static List<Integer> boyerMoore(CharSequence pattern, CharSequence text, CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("ERROR: Please provide a valid pattern.");
        }
        if (text == null || comparator == null) {
            throw new IllegalArgumentException("Error: Please provide a valid text and/or pattern");
        }

        int n = text.length();
        int m = pattern.length();
        ArrayList<Integer> occurrences = new ArrayList<Integer>();
        if (m > n) {
            return occurrences;
        }

        int lastOccurrenceIndex;
        int i = 0;
        Map<Character, Integer> lastOccurrenceTable = buildLastTable(pattern);
        while (i <= n - m) {
            int j = m - 1;
            while (j >= 0 && comparator.compare(text.charAt(i + j), pattern.charAt(j)) == 0) {
                j--;
            }
            if (j == -1) {
                occurrences.add(i);
                i++;
            } else {
                lastOccurrenceIndex = getOrDefault(lastOccurrenceTable, text, i + j);
                if (lastOccurrenceIndex < j) {
                    i = i + j - lastOccurrenceIndex;
                } else {
                    i++;
                }
            }
        }
        return occurrences;

        /*
        while (i <= n - m) {
            if (comparator.compare(text.charAt(i + j), pattern.charAt(j)) == 0) {
                if (j == 0) {
                    occurrences.add(i);
                    i++;
                    j = m - 1;
                } else {
                    j--;
                }
            } else {
                lastOccurrenceIndex = getOrDefault(lastOccurrenceTable, text.charAt(i + j), -1);
                if (lastOccurrenceIndex <= j) {
                    i = i + (j - lastOccurrenceIndex);
                    j = m -1;
                } else {
                    i = i + j + 1;
                }
            }
        }

        return occurrences;

         */

    }

    /**
     * Builds last occurrence table that will be used to run the Boyer Moore
     * algorithm.
     *
     * Note that each char x will have an entry at table.get(x).
     * Each entry should be the last index of x where x is a particular
     * character in your pattern.
     * If x is not in the pattern, then the table will not contain the key x,
     * and you will have to check for that in your Boyer Moore implementation.
     *
     * Ex. pattern = octocat
     *
     * table.get(o) = 3
     * table.get(c) = 4
     * table.get(t) = 6
     * table.get(a) = 5
     * table.get(everything else) = null, which you will interpret in
     * Boyer-Moore as -1
     *
     * If the pattern is empty, return an empty map.
     *
     * @param pattern a pattern you are building last table for
     * @return a Map with keys of all of the characters in the pattern mapping
     * to their last occurrence in the pattern
     * @throws java.lang.IllegalArgumentException if the pattern is null
     */
    public static Map<Character, Integer> buildLastTable(CharSequence pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("ERROR: A pattern must be given");
        }

        HashMap<Character, Integer> lastOccurrenceTable = new HashMap<>();
        int m = pattern.length();
        for (int i = 0; i <= m - 1; i++) {
            lastOccurrenceTable.put(pattern.charAt(i), i);
        }

        return lastOccurrenceTable;
    }

    /**
     * Custom getOrDefault takes in the table, and determines if the
     * key is within. In the case it is, it returns the value associated
     * it, else it returns -1.
     *
     * @param lastOccurrenceTable The last occurrence table created at the
     *                            beginning of the method
     * @param text The text being compared to
     * @param index The index of the character that we want to know if
     *              it is in the LOT
     * @return The index shift modifier or -1 in the case it doesn't exist.
     */
    private static int getOrDefault(Map<Character, Integer> lastOccurrenceTable, CharSequence text, int index) {
        if (lastOccurrenceTable.containsKey(text.charAt(index))) {
            return lastOccurrenceTable.get(text.charAt(index));
        } else {
            return -1;
        }
    }


    /**
     * Prime base used for Rabin-Karp hashing.
     * DO NOT EDIT!
     */
    private static final int BASE = 113;

    /**
     * Runs the Rabin-Karp algorithm. This algorithms generates hashes for the
     * pattern and compares this hash to substrings of the text before doing
     * character by character comparisons.
     *
     * When the hashes are equal and you do character comparisons, compare
     * starting from the beginning of the pattern to the end, not from the end
     * to the beginning.
     *
     * You must use the Rabin-Karp Rolling Hash for this implementation. The
     * formula for it is:
     *
     * sum of: c * BASE ^ (pattern.length - 1 - i)
     *   c is the integer value of the current character, and
     *   i is the index of the character
     *
     * We recommend building the hash for the pattern and the first m characters
     * of the text by starting at index (m - 1) to efficiently exponentiate the
     * BASE. This allows you to avoid using Math.pow().
     *
     * Note that if you were dealing with very large numbers here, your hash
     * will likely overflow; you will not need to handle this case.
     * You may assume that all powers and calculations CAN be done without
     * overflow. However, be careful with how you carry out your calculations.
     * For example, if BASE^(m - 1) is a number that fits into an int, it's
     * possible for BASE^m will overflow. So, you would not want to do
     * BASE^m / BASE to calculate BASE^(m - 1).
     *
     * Ex. Hashing "bunn" as a substring of "bunny" with base 113
     * = (b * 113 ^ 3) + (u * 113 ^ 2) + (n * 113 ^ 1) + (n * 113 ^ 0)
     * = (98 * 113 ^ 3) + (117 * 113 ^ 2) + (110 * 113 ^ 1) + (110 * 113 ^ 0)
     * = 142910419
     *
     * Another key point of this algorithm is that updating the hash from
     * one substring to the next substring must be O(1). To update the hash,
     * subtract the oldChar times BASE raised to the length - 1, multiply by
     * BASE, and add the newChar as shown by this formula:
     * (oldHash - oldChar * BASE ^ (pattern.length - 1)) * BASE + newChar
     *
     * Ex. Shifting from "bunn" to "unny" in "bunny" with base 113
     * hash("unny") = (hash("bunn") - b * 113 ^ 3) * 113 + y
     *              = (142910419 - 98 * 113 ^ 3) * 113 + 121
     *              = 170236090
     *
     * Keep in mind that calculating exponents is not O(1) in general, so you'll
     * need to keep track of what BASE^(m - 1) is for updating the hash.
     *
     * Do NOT use Math.pow() in this method.
     *
     * @param pattern    a string you're searching for in a body of text
     * @param text       the body of text where you search for pattern
     * @param comparator you MUST use this to check if characters are equal
     * @return list containing the starting index for each match found
     * @throws java.lang.IllegalArgumentException if the pattern is null or has
     *                                            length 0
     * @throws java.lang.IllegalArgumentException if text or comparator is null
     */
    public static List<Integer> rabinKarp(CharSequence pattern, CharSequence text, CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("ERROR: A Pattern must be given");
        }
        if (text == null || comparator == null) {
            throw new IllegalArgumentException("ERROR: A text and/or comparator must be given");
        }

        int patternHash = 0;
        int textHash = 0;
        int runningFactor = 1;
        ArrayList<Integer> occurrences = new ArrayList<Integer>();
        int n = text.length();
        int m = pattern.length();
        if (n < m) {
            return occurrences;
        }

        for (int i = m - 1; i >= 0; i--) {
            patternHash = patternHash + (pattern.charAt(i) * runningFactor);
            textHash = textHash + (text.charAt(i) * runningFactor);
            if (i != 0) {
                runningFactor = runningFactor * BASE;
            }
        }

        int i = 0;
        while (i <= n - m) {
            if (patternHash == textHash) {
                int j = 0;
                while (j < m && comparator.compare(text.charAt(i + j), pattern.charAt(j)) == 0) {
                    j++;
                }
                if (j == m) {
                    occurrences.add(i);
                }
            }
            i++;
            if (i <= n - m) {
                textHash = BASE * (textHash - text.charAt(i - 1) * runningFactor) + text.charAt(i - 1 + m);
            }
        }

        /*
        int i = 0;
        int j = 0;
        while ( i <= n - m) {
            if (patternHash == textHash) {
                while (j <= m - 1) {
                    if (comparator.compare(text.charAt(i + j), pattern.charAt(j)) == 0) {
                        if (j == m - 1) {
                            occurrences.add(i);
                            i++;
                        }
                        j++;
                    }
                }
            } else {
                textHash = (textHash - (text.charAt(i - 1) * runningFactor)) + (text.charAt(i - 1 + m) * runningFactor);
            }
            i++;
            j = 0;
        }

         */

        return occurrences;
    }


    /**
     * This method is OPTIONAL and for extra credit only.
     *
     * The Galil Rule is an addition to Boyer Moore that optimizes how we shift the pattern
     * after a full match. Please read Extra Credit: Galil Rule section in the homework pdf for details.
     *
     * Make sure to implement the buildLastTable() method and buildFailureTable() method
     * before implementing this method.
     *
     * @param pattern    the pattern you are searching for in a body of text
     * @param text       the body of text where you search for the pattern
     * @param comparator you MUST use this to check if characters are equal
     * @return list containing the starting index for each match found
     * @throws java.lang.IllegalArgumentException if the pattern is null or has
     *                                            length 0
     * @throws java.lang.IllegalArgumentException if text or comparator is null
     */
    public static List<Integer> boyerMooreGalilRule(CharSequence pattern, CharSequence text,
                                                    CharacterComparator comparator) {
        if (pattern == null || pattern.length() == 0) {
            throw new IllegalArgumentException("ERROR: Please provide a valid pattern.");
        }
        if (text == null || comparator == null) {
            throw new IllegalArgumentException("Error: Please provide a valid text and/or pattern");
        }


        int n = text.length();
        int m = pattern.length();
        ArrayList<Integer> occurrences = new ArrayList<Integer>();
        if (m > n) {
            return occurrences;
        }
        Map<Character, Integer> lastOccurrenceTable = buildLastTable(pattern);
        int period = getPeriod(pattern, comparator);
        int lastOccurrenceIndex;
        int i = 0;
        int w = 0;
        int j;
        while (i <= n - m) {
            j = m - 1;
            while (j >= w && comparator.compare(text.charAt(i + j), pattern.charAt(j)) == 0) {
                j--;
            }
            if (j < w) {
                occurrences.add(i);
                w = m - period;
                i = i + period;
            } else {
                w = 0;
                lastOccurrenceIndex = getOrDefault(lastOccurrenceTable, text, i + j);
                if (lastOccurrenceIndex < j) {
                    i = i + j - lastOccurrenceIndex;
                } else {
                    i++;
                }
            }
        }

        return occurrences;

    }

    /**
     * Calculates the period of a passed pattern.
     *
     * Used for the Galil Rule implementation of Boyer-Moore
     * pattern matching.
     *
     * @param pattern The pattern to be analyzed for pattern matching
     * @param comparator Used to compare characters
     * @return The period to shift by anytime there is a match
     */
    private static int getPeriod(CharSequence pattern, CharacterComparator comparator) {
        if (pattern == null || comparator == null) {
            throw new IllegalArgumentException("ERROR: Pattern nor Compartator can equal null");
        }

        int m = pattern.length();
        int i = 0;
        int j = 1;
        int[] failureTable = new int[m];
        failureTable[0] = 0;
        while (j < m) {
            if (comparator.compare(pattern.charAt(i), pattern.charAt(j)) == 0) {
                failureTable[j] = i + 1;
                i++;
                j++;
            } else if (i == 0) {
                failureTable[j] = 0;
                j++;
            } else {
                i = failureTable[i - 1];
            }
        }

        int period = m - failureTable[m - 1];

        return period;
    }
}

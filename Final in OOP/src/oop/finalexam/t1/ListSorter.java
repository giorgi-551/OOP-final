package oop.finalexam.t1;

import java.util.*;

/**
 * ListSorter - A comprehensive program for combining lists using index-based access
 *
 * This program demonstrates the concept of using numerical values from list1
 * as indices to access string elements from list2. The algorithm creates combinations
 * in the original order of list1 while handling edge cases where index values
 * might exceed the bounds of list2.
 *
 * Algorithm Overview:
 * 1. Two input lists are provided: list1 (containing integers) and list2 (containing strings)
 * 2. Each integer in list1 represents an index (0-based) to access elements from list2
 * 3. For each value in list1 (in original order), use it as index to get string from list2
 * 4. Create combinations in format: list2[list1[i]] + list1[i]
 * 5. If an index from list1 exceeds the bounds of list2, that combination is skipped
 * 6. The resulting combinations are displayed in the same order as list1 values appear
 *
 * Error Handling:
 * - Handles cases where list1 contains indices larger than the size of list2
 * - Handles cases where lists have different sizes
 * - Provides informative messages about skipped elements
 * - Ensures program continues execution even with invalid indices
 *
 */
public class ListSorter {

    /**
     * List containing numerical indices that will be used to access elements from list2
     * These numbers represent 0-based indices for sorting purposes
     */
    private static List<Integer> list1;

    /**
     * List containing string elements that will be sorted based on indices from list1
     * Elements from this list will be accessed using indices from list1
     */
    private static List<String> list2;

    /**
     * Main method - Entry point of the program
     * Initializes the lists with predefined values and executes the sorting algorithm
     *
     * @param args Command line arguments (not used in this implementation)
     */
    public static void main(String[] args) {
        // Initialize lists with predefined values
        initializeLists();

        // Display original lists
        displayOriginalLists();

        // Process and combine the lists
        List<String> combinedResult = processList();

        // Display the final combined result
        displayCombinedResult(combinedResult);

        // Process deletions based on list1 values as indices
        List<String> finalResult = processDeletions(combinedResult);

        // Display the final result after deletions
        displayFinalResult(finalResult);
    }

    /**
     * Initializes both lists with predefined values as specified in the requirements
     * list1: Contains integers [8, 3, 2, 6, 4, 1, 7, 9, 10, 5]
     * list2: Contains strings ["trt", "kyg", "amk", "mrp", "gon", "iud", "qoq", "ztn", "bvi", "aih", "onf", "zwu"]
     */
    private static void initializeLists() {
        // Initialize list1 with predefined integer values
        list1 = Arrays.asList(8, 3, 2, 6, 4, 1, 7, 9, 10, 5);

        // Initialize list2 with predefined string values
        list2 = Arrays.asList("trt", "kyg", "amk", "mrp", "gon", "iud", "qoq", "ztn", "bvi", "aih", "onf", "zwu");
    }

    /**
     * Displays the original lists before processing
     * Provides clear output showing the initial state of both lists
     */
    private static void displayOriginalLists() {
        System.out.println("___ Original Lists ___");
        System.out.println("List1 (indices): " + list1);
        System.out.println("List2 (strings): " + list2);
        System.out.println("List2 size: " + list2.size() + " elements (indices 0-" + (list2.size() - 1) + ")");
        System.out.println();
    }

    /**
     * Core algorithm that processes the lists and creates combined pairs
     *
     * Algorithm Steps:
     * 1. Iterate through each value in list1 (in original order)
     * 2. For each value, use it as an index to access list2
     * 3. Check if the index value is within bounds of list2
     * 4. If valid, create a combined string with format: list2[index] + index
     * 5. If invalid, skip the pair and log an error message
     * 6. Return the list of combined strings in original List1 order
     *
     * Error Handling:
     * - Checks if index from list1 is within bounds of list2 (0 <= index < list2.size())
     * - Skips invalid indices and continues processing
     * - Provides detailed error messages for debugging
     *
     * @return List of combined strings in original List1 order
     */
    private static List<String> processList() {
        List<String> combinedStrings = new ArrayList<>();
        int skippedCount = 0;

        System.out.println("___ Processing Phase ___");

        // Process each value in list1 (using it as index+1 for list2)
        for (int i = 0; i < list1.size(); i++) {
            int indexValue = list1.get(i);
            int actualIndex = indexValue + 1; // Add 1 to the index value

            // Error handling: Check if actualIndex is valid
            if (actualIndex >= 0 && actualIndex < list2.size()) {
                String element = list2.get(actualIndex);
                String combined = element + indexValue;
                combinedStrings.add(combined);
                System.out.println("✓ Valid: list1[" + i + "] = " + indexValue + " -> list2[" + actualIndex + "] = \"" + element + "\" -> " + combined);
            } else {
                // Handle error case: index out of bounds
                skippedCount++;
                System.out.println("✗ Skipped: list1[" + i + "] = " + indexValue + " (actual index: " + actualIndex + ") is out of bounds (list2 size: " + list2.size() + ")");
            }
        }

        System.out.println("\nProcessing Summary:");
        System.out.println("- Valid combinations created: " + combinedStrings.size());
        System.out.println("- Invalid combinations skipped: " + skippedCount);
        System.out.println();

        return combinedStrings;
    }

    /**
     * Processes deletions from List3 based on values from List1 used as indices
     *
     * Algorithm:
     * 1. Take each value from List1 as an index to delete from List3
     * 2. Sort deletion indices in descending order to avoid index shifting issues
     * 3. Delete elements at those positions from List3
     * 4. Skip indices that are out of bounds for List3
     *
     * @param list3 The combined list to process deletions from
     * @return The final list after deletions
     */
    private static List<String> processDeletions(List<String> list3) {
        // Create a mutable copy of list3
        List<String> mutableList3 = new ArrayList<>(list3);

        // Get unique deletion indices from list1 and sort them in descending order
        // This prevents index shifting issues when deleting multiple elements
        Set<Integer> deletionIndicesSet = new HashSet<>(list1);
        List<Integer> deletionIndices = new ArrayList<>(deletionIndicesSet);
        deletionIndices.sort(Collections.reverseOrder());

        // Process deletions in descending order
        for (int index : deletionIndices) {
            if (index >= 0 && index < mutableList3.size()) {
                mutableList3.remove(index);
            }
        }

        return mutableList3;
    }

    /**
     * Displays the combined result (List3) before deletions
     * Shows each combination as string_element + index_value in original List1 order
     * Results are shown in the same order as List1 values appear
     *
     * @param combinedResult List of combined strings to display
     */
    private static void displayCombinedResult(List<String> combinedResult) {
        System.out.println("___ Combined Result (List3) Before Deletions ___");

        if (combinedResult.isEmpty()) {
            System.out.println("No valid combinations to display due to error handling.");
            return;
        }

        System.out.print("List3: ");
        for (int i = 0; i < combinedResult.size(); i++) {
            System.out.print(combinedResult.get(i));
            if (i < combinedResult.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();

        // Additional detailed output
        System.out.println("\nDetailed breakdown (in List1 order):");
        for (int i = 0; i < combinedResult.size(); i++) {
            System.out.println("- Position " + i + ": " + combinedResult.get(i));
        }
        System.out.println();
    }

    /**
     * Displays the final result after all deletions
     *
     * @param finalResult The final list after deletions
     */
    private static void displayFinalResult(List<String> finalResult) {
        System.out.println("=== Final Result After Deletions ===");

        if (finalResult.isEmpty()) {
            System.out.println("All elements have been deleted from List3.");
            return;
        }

        System.out.print("Final List: ");
        for (int i = 0; i < finalResult.size(); i++) {
            System.out.print(finalResult.get(i));
            if (i < finalResult.size() - 1) {
                System.out.print(" ");
            }
        }
        System.out.println();
    }
}
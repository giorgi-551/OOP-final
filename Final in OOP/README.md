Final exam

Overview
This is a Java program that demonstrates **list manipulation** using index-based access. The program combines two lists and then performs selective deletions based on index values.

How it Works

Input Lists
List1: [8, 3, 2, 6, 4, 1, 7, 9, 10, 5] (integers used as indices)
List2: ["trt", "kyg", "amk", "mrp", "gon", "iud", "qoq", "ztn", "bvi", "aih", "onf", "zwu"] (strings)

Algorithm Steps

1. Phase 1 - Combination:
   - Take each value from List1
   - Add 1 to it (so 8 becomes 9)
   - Use this as index to access List2
   - Combine: List2[index] + original_value
   - Example: 8 → List2[9] = "aih" → "aih8"

2. Phase 2 - Deletion:
   - Use List1 values as indices to delete from List3
   - Delete elements at positions: 8, 3, 2, 6, 4, 1, 7, 9, 5
   - Skip position 10 (out of bounds)
   - Only element at position 0 survives

Example Output

List3: [aih8 gon3 mrp2 ztn6 iud4 kyg1 bvi7 aih9 onf10 qoq5]
Final List: [aih8]

Code Structure

Main Methods
- main() - Entry point
- initializeLists() - Sets up the data
- processList() - Creates combinations
- processDeletions() - Removes elements
- displayFinalResult() - Shows output

Key Features
- Error handling for out-of-bounds indices
- Detailed logging of each step
- Clean separation of combination and deletion phases

Why This Program is Useful

This program demonstrates:
- Index manipulation techniques
- List processing algorithms
- Error handling best practices
- Step-by-step debugging approach
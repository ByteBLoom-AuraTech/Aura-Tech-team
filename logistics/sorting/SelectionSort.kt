package logistics.sorting

/**
 * Returns the higher priority between two values.
 * URGENT is the highest, then STANDARD, then LOW.
 * Converts both to uppercase first so the comparison isn't case-sensitive.
 */
fun getHigherPriority(firstPriority: String, secondPriority: String): String {
    // Normalize both values to uppercase so the comparison isn't case-sensitive
    val priorityOne = firstPriority.trim().uppercase()
    val priorityTwo = secondPriority.trim().uppercase()

    // If either one is URGENT, no need to check further - it wins immediately
    if (priorityOne == "URGENT" || priorityTwo == "URGENT") {
        return "URGENT"
    }

    // Reaching here means neither is URGENT, so check for STANDARD
    if (priorityOne == "STANDARD" || priorityTwo == "STANDARD") {
        return "STANDARD"
    }

    // If none of the conditions above matched, both must be LOW
    return "LOW"
}
/*** Checks whether two priority values are the same, ignoring case and spacing.*/
fun isSamePriority(firstPriority: String, secondPriority: String): Boolean {
    // Normalize both values before comparing so case/spacing differences don't matter
    return firstPriority.trim().uppercase() == secondPriority.trim().uppercase()
}

/**
 * Searches the unsorted portion of the list (from startIndex to the end)
 * and returns the index of the best candidate (highest priority, then highest weight).
 */
fun findBestIndex(packagesList: MutableList<List<String>>, startIndex: Int): Int {
    val listSize = packagesList.size
    var targetIndex = startIndex
    for (searchIndex in startIndex + 1 until listSize) {
        val currentExamined = packagesList[searchIndex]
        val bestFoundSoFar = packagesList[targetIndex]
        // 1. Extract the priority string from the 3rd column (Index 2)
        val currentPriorityStr = currentExamined[2]
        val bestPriorityStr = bestFoundSoFar[2]
        // 2. First Check: If the current package has a higher priority than the best one found so far
        if (isHigherPriority(currentPriorityStr, bestPriorityStr)) {
            targetIndex = searchIndex
        // 3. Second Check: If priorities are identical, move to comparing weights
        } else if (isSamePriority(currentPriorityStr, bestPriorityStr)) {
            // Convert weight strings safely to double values for mathematical comparison
            val currentWeight = currentExamined[1].toDoubleOrNull() ?: 0.0
            val bestWeight = bestFoundSoFar[1].toDoubleOrNull() ?: 0.0
        // 4. Compare weights in descending order (heavy weights first)
            if (currentWeight > bestWeight) {
                targetIndex = searchIndex
        // 5. Maintain algorithm stability if both priority and weight match perfectly
            } else if (currentWeight == bestWeight) {
                if (searchIndex < targetIndex) {
                    targetIndex = searchIndex
                }
            }
        }
    }
    // Return the final index of the best package found in this pass
    return targetIndex
}

/**
 * Swaps the elements at the two given indexes in the list.
 */
fun swapPackages(packagesList: MutableList<List<String>>, indexA: Int, indexB: Int) {
    // 1. Save indexA element in a temporary variable so it doesn't get overwritten
    val temporaryHolder = packagesList[indexA]
    packagesList[indexA] = packagesList[indexB]
    // 2. Complete the swap by placing the saved element into indexB
    packagesList[indexB] = temporaryHolder
}

/**
 * Manual Selection Sort: repeatedly finds the best remaining candidate
 * and swaps it into its correct position.
 */
fun selectionSort(packagesList: MutableList<List<String>>) {

    val listSize = packagesList.size
         // 1. Loop through the list starting from the first element (Index 0) to the second to last element
    for (currentIndex in 0 until listSize - 1) {
        // 2. Get the index of the best package in the unsorted part
        val targetIndex = findBestIndex(packagesList, currentIndex)
        // 3. Only swap if a better package was found at a different position
        if (targetIndex != currentIndex) {
            swapPackages(packagesList, currentIndex, targetIndex)
        }
    }
}
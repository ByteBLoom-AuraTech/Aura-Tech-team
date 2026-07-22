package logistics.sorting

/**
 * Returns the higher priority between two values.
 * URGENT is the highest, then STANDARD, then LOW.
 */
fun getHigherPriority(firstPriority: String, secondPriority: String): String {
    val priorityOne = firstPriority.trim().uppercase()
    val priorityTwo = secondPriority.trim().uppercase()

    if (priorityOne == "URGENT" || priorityTwo == "URGENT") {
        return "URGENT"
    }

    if (priorityOne == "STANDARD" || priorityTwo == "STANDARD") {
        return "STANDARD"
    }

    return "LOW"
}
/*** Checks whether two priority values are the same, ignoring case and spacing.*/
fun isSamePriority(firstPriority: String, secondPriority: String): Boolean {
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
        val currentPriorityStr = currentExamined[2]
        val bestPriorityStr = bestFoundSoFar[2]
        if (isHigherPriority(currentPriorityStr, bestPriorityStr)) {
            targetIndex = searchIndex
        } else if (isSamePriority(currentPriorityStr, bestPriorityStr)) {
            val currentWeight = currentExamined[1].toDoubleOrNull() ?: 0.0
            val bestWeight = bestFoundSoFar[1].toDoubleOrNull() ?: 0.0
            if (currentWeight > bestWeight) {
                targetIndex = searchIndex
            } else if (currentWeight == bestWeight) {
                if (searchIndex < targetIndex) {
                    targetIndex = searchIndex
                }
            }
        }
    }
    return targetIndex
}


/**
 * Manual Selection Sort: repeatedly finds the best remaining candidate
 * and swaps it into its correct position.
 */
fun selectionSort(packagesList: MutableList<List<String>>) {
    val listSize = packagesList.size

    for (currentIndex in 0 until listSize - 1) {
        val targetIndex = findBestIndex(packagesList, currentIndex)

        if (targetIndex != currentIndex) {
            val selectedPackage = packagesList[targetIndex]

            for (i in targetIndex downTo currentIndex + 1) {
                packagesList[i] = packagesList[i - 1]
            }
            packagesList[currentIndex] = selectedPackage
        }
    }
}
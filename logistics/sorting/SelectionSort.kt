package logistics.sorting


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
fun isSamePriority(firstPriority: String, secondPriority: String): Boolean {
    return firstPriority.trim().uppercase() == secondPriority.trim().uppercase()
}

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
            }
        }
    }

    return targetIndex
}


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
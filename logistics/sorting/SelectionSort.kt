package logistics.sorting

fun sortPackagesByPriority(packagesList: MutableList<List<String>>) {
    val listSize = packagesList.size

    for (currentIndex in 0 until listSize - 1) {
        val bestIndex = findBestIndex(packagesList, currentIndex)

        if (bestIndex != currentIndex) {
            val selectedPackage = packagesList[bestIndex]

            for (i in bestIndex downTo currentIndex + 1) {
                packagesList[i] = packagesList[i - 1]
            }

            packagesList[currentIndex] = selectedPackage
        }
    }
}

fun findBestIndex(packagesList: MutableList<List<String>>, startIndex: Int): Int {
    val listSize = packagesList.size
    var targetIndex = startIndex

    for (searchIndex in startIndex + 1 until listSize) {
        val currentPriorityStr = packagesList[searchIndex][2]
        val bestPriorityStr = packagesList[targetIndex][2]

        val higherPriority = getHigherPriority(currentPriorityStr, bestPriorityStr)

        if (higherPriority == currentPriorityStr && !isSamePriority(currentPriorityStr, bestPriorityStr)) {
            targetIndex = searchIndex
        } else if (isSamePriority(currentPriorityStr, bestPriorityStr)) {
            val currentWeight = packagesList[searchIndex][1].toDoubleOrNull() ?: 0.0
            val bestWeight = packagesList[targetIndex][1].toDoubleOrNull() ?: 0.0

            if (currentWeight > bestWeight) {
                targetIndex = searchIndex
            }
        }
    }
    return targetIndex
}

fun getHigherPriority(firstPriority: String, secondPriority: String): String {
    return if (getPriorityRank(firstPriority) >= getPriorityRank(secondPriority)) firstPriority else secondPriority
}

fun isSamePriority(firstPriority: String, secondPriority: String): Boolean {
    return getPriorityRank(firstPriority) == getPriorityRank(secondPriority)
}

fun getPriorityRank(priority: String): Int {
    return when (priority.trim().uppercase()) {
        "URGENT" -> 3
        "STANDARD" -> 2
        "LOW" -> 1
        else -> 0
    }
}
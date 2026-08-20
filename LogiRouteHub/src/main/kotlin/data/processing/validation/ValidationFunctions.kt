package data.processing.validation

const val INVALID_DOUBLE_VALUE = -1.0
const val INVALID_INT_VALUE = -1
private const val MIN_POSITIVE_DOUBLE = 0.0
private const val MIN_NON_NEGATIVE_INT = 0


fun hasExpectedColumnCount(columns: List<String>, expectedCount: Int): Boolean {
    return columns.size == expectedCount
}

fun isNotBlank(value: String): Boolean {
    return value.isNotBlank()
}

fun parsePositiveDoubleOrInvalid(value: String): Double {
    val parsedNumber = value.trim().toDoubleOrNull()
    return if (parsedNumber != null && parsedNumber >MIN_POSITIVE_DOUBLE) {
        parsedNumber
    } else {
        INVALID_DOUBLE_VALUE
    }
}

fun parseNonNegativeIntOrInvalid(value: String): Int {
    val parsedNumber = value.trim().toIntOrNull()
    return if (parsedNumber != null && parsedNumber >= MIN_NON_NEGATIVE_INT) {
        parsedNumber
    } else {
        INVALID_INT_VALUE
    }
}
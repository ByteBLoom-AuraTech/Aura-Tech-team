packages logistics

// =========================================================================
// DataModels.kt
// يحتوي على كل الكلاسات المشتركة بين أعضاء الفريق.
// أي تعديل على هاد الملف لازم يتم الاتفاق عليه مسبقًا مع الكل
// لأنه باقي الملفات (DataValidator.kt و MainPipeline.kt) بتعتمد عليه مباشرة.
// =========================================================================

enum class Priority(val rank: Int) {
    URGENT(3),
    STANDARD(2),
    LOW(1);

    companion object {
        /** تحويل نص لأولوية بدون حساسية لحالة الأحرف، وأي قيمة غريبة ترجع LOW. */
        fun fromString(raw: String): Priority {
            return when (raw.trim().uppercase()) {
                "URGENT" -> URGENT
                "STANDARD" -> STANDARD
                "LOW" -> LOW
                else -> LOW
            }
        }
    }
}

data class Package(
    val packageId: String,
    val weight: Double,
    val priority: Priority,
    val destinationHubId: String
)

data class Warehouse(
    val warehouseId: String,
    val name: String,
    val capacity: Double,
    val location: String
)

data class RouteRecord(
    val routeId: String,
    val originId: String,
    val destinationId: String,
    val distance: Double
)

data class Vehicle(
    val vehicleId: String,
    val type: String,
    val capacity: Double,
    val status: String
)

package logistics.parsers


data class PackageRow(
    val id: String,
    val weight: Double,
    val destinationHubId: String,
    val priority: String
)
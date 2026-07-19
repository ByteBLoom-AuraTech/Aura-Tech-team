package logistics

enum class Priority(val rank: Int) {
    URGENT(3),
    STANDARD(2),
    LOW(1);

    companion object {

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

fun main() {

    val files = arrayOf("resources/fleet.csv", "resources/packages.csv", "resources/routes.csv", "resources/warehouses.csv")

    for (file in files) {
        println("===========================================")
        println("------------------ $file ------------------")
        println("===========================================")
        val fil = java.io.File(file)
        val reader = fil.bufferedReader()
        val header = reader.readLine()
        var NumberOfline = 0
        var line = reader.readLine()
        NumberOfline++
//============================================================================
        while (line != null) {
            var start = 0
            var head = 0
            while (start < line.length) {
                if (line[start] == ',') {
                    head++
                }
                start++
            }
            line = reader.readLine()
            NumberOfline++

//============================================================================
            if (line.length == 0) {
                line = reader.readLine()
                NumberOfline++
            } else {

                var start = 0
                var clean = " "
                var segments = 0

                while (start < line.length) {
                    if (line[start] != ' ') {
                        clean = clean + line[start]
                    }
                    if (line[start] == ',') {
                        segments++
                    }
                    start++
                }

                if (segments != head) {
                    if (segments < head) {
                        println("WARNING--- : in line number $NumberOfline these is a deleted value")
                    } else if (segments > head) {
                        println("WARNING--- : in line number $NumberOfline these is an extra value")
                    }
                } else {
                    var indexOfClean = 0
                    var word = " "
                    var isNumeric = true
                    while (indexOfClean <= clean.length) {
                        if (indexOfClean <= clean.length && clean[indexOfClean] != ',') {
                            word = word + clean[indexOfClean]
                        } else {
                            var charIndex = 0
                            var dotCount = 0
                            while (charIndex < word.length) {
                                if (word[charIndex] == '.') {
                                    dotCount++
                                } else if (word[charIndex] < '0' || word[charIndex] > '9') {
                                    isNumeric = false
                                }
                                charIndex++
                            }
                            if (isNumeric == false || dotCount > 1 || word.length == 0) {
                                println("WARNING--- : in line number $NumberOfline malformed numeric data!")
                            }
                            word = ""
                        }
                        indexOfClean++
                    }
                }
                line = reader.readLine()
            }
        }
    }
}
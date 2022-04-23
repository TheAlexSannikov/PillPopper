package sannikov.a.stonerstopwatch.data

/**
 * Stores information about each drug
 */
enum class Drug(
    val drugName: String,
    val periodHrs: Int,
    val maxDosageMgPerPeriod: Int,
    val maxDosageMgPerDay: Int
) {
    ACETAMINOPHEN(
        drugName = "acetaminophen",
        periodHrs = 6,
        maxDosageMgPerPeriod = 1000,
        maxDosageMgPerDay = 3000
    ),
    IBUPROFEN(
        drugName = "ibuprofen",
        periodHrs = 6,
        maxDosageMgPerPeriod = 400,
        maxDosageMgPerDay = 1200
    )
}

package sannikov.a.stonerstopwatch.data

/**
 * Stores information about each drug
 */
sealed class Drug(
    val name: String,
    val periodHrs: Int,
    val maxDosageMgPerPeriod: Int,
    val maxDosageMgPerDay: Int
) {
    object Acetaminophen : Drug(
        name = "acetaminophen",
        periodHrs = 6,
        maxDosageMgPerPeriod = 1000,
        maxDosageMgPerDay = 3000
    )
}

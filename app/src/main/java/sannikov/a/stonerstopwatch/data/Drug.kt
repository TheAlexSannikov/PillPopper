package sannikov.a.stonerstopwatch.data

import sannikov.a.stonerstopwatch.R

/**
 * Stores information about each drug
 */
enum class Drug(
    val drugName: String,
    val periodHrs: Int,
    val maxDosageMgPerPeriod: Int,
    val maxDosageMgPerDay: Int,
    val imageId: Int,
) {
    ACETAMINOPHEN(
        drugName = "acetaminophen",
        periodHrs = 6,
        maxDosageMgPerPeriod = 1000,
        maxDosageMgPerDay = 3000,
        imageId = R.drawable.ic_acetaminophen
    ),
    IBUPROFEN(
        drugName = "ibuprofen",
        periodHrs = 6,
        maxDosageMgPerPeriod = 400,
        maxDosageMgPerDay = 1200,
        imageId = R.drawable.ic_ibuprofen
    )
}

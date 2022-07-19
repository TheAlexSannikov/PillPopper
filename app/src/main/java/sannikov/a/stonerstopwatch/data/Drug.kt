package sannikov.a.stonerstopwatch.data

import sannikov.a.stonerstopwatch.R

/**
 * Stores information about each drug
 */
enum class Drug(
    val drugName: String,
    val defaultDosageMg: Int,
    val periodHrs: Int,
    val maxDosageMgPerPeriod: Int,
    val maxDosageMgPerDay: Int,
    val imageId: Int,
) {
    ACETAMINOPHEN(
        drugName = "acetaminophen",
        defaultDosageMg = 500,
        periodHrs = 6,
        maxDosageMgPerPeriod = 1000,
        maxDosageMgPerDay = 3000,
        imageId = R.drawable.ic_acetaminophen
    ),
    IBUPROFEN(
        drugName = "ibuprofen",
        defaultDosageMg = 200,
        periodHrs = 6,
        maxDosageMgPerPeriod = 400,
        maxDosageMgPerDay = 1200,
        imageId = R.drawable.ic_ibuprofen
    ),
    BENADRYL(
        drugName = "benadryl",
        defaultDosageMg = 25,
        periodHrs = 6,
        maxDosageMgPerPeriod = 50,
        maxDosageMgPerDay = 150,
        imageId = R.drawable.ic_benadryl
    )

}

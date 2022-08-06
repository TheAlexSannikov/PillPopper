package sannikov.a.stonerstopwatch.data

import sannikov.a.stonerstopwatch.R

/**
 * Default information for each drug
 */
enum class Drug(
    val drugName: String,
    val dosageMg: Int,
    val periodHrs: Int,
    val maxDosageMgPerPeriod: Int,
    val maxDosageMgPerDay: Int,
    val imageId: Int,
) {
    ACETAMINOPHEN(
        drugName = "acetaminophen",
        dosageMg = 500,
        periodHrs = 6,
        maxDosageMgPerPeriod = 1000,
        maxDosageMgPerDay = 3000,
        imageId = R.drawable.ic_acetaminophen
    ),
    IBUPROFEN(
        drugName = "ibuprofen",
        dosageMg = 200,
        periodHrs = 6,
        maxDosageMgPerPeriod = 400,
        maxDosageMgPerDay = 1200,
        imageId = R.drawable.ic_ibuprofen
    ),
    BENADRYL(
        drugName = "benadryl",
        dosageMg = 25,
        periodHrs = 6,
        maxDosageMgPerPeriod = 50,
        maxDosageMgPerDay = 150,
        imageId = R.drawable.ic_benadryl
    )
}

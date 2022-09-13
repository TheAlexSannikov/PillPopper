package sannikov.a.stonerstopwatch.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import sannikov.a.stonerstopwatch.R
import kotlin.math.max

/**
 * Information for each drug.
 */
@Entity(tableName = "Drug")
data class Drug(
    @NonNull @ColumnInfo(name = "drugName") var drugName: String,
    @NonNull @ColumnInfo(name = "dosageMg") var dosageMg: Int,
    @NonNull @ColumnInfo(name = "periodHrs") var periodHrs: Int,
    @NonNull @ColumnInfo(name = "maxDosageMgPerPeriod") var maxDosageMgPerPeriod: Int,
    @NonNull @ColumnInfo(name = "maxDosageMgPerDay") var maxDosageMgPerDay: Int,
    @NonNull @ColumnInfo(name = "imageId") var imageId: Int,
    @NonNull @ColumnInfo(name = "ordinal") var ordinal: Int, // dictates how drugs are ordered
    @NonNull @ColumnInfo(name = "isAddNewDrug") val isAddNewDrug: Boolean = false, // only true for ADD_NEW_DRUG
    @NonNull @PrimaryKey(autoGenerate = true) val drugId: Int = 0
) {
    companion object {
        val ADD_NEW_DRUG = Drug(
            drugName = "Add new Drug",
            dosageMg = 0,
            periodHrs = 0,
            maxDosageMgPerPeriod = 0,
            maxDosageMgPerDay = 0,
            imageId = R.drawable.ic_baseline_add_24,
            ordinal = 0,
            isAddNewDrug = true
        )
    }
}

/**
 * enum class Drug(
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
 */

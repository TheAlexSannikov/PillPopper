package sannikov.a.stonerstopwatch.data

import androidx.annotation.NonNull
import androidx.room.*

/**
 * Stores information for an individual pill taken by the user. Storable in Room
 */
@Entity(tableName = "Pill")
data class Pill(
    @NonNull @ColumnInfo(name = "drugId") val drugId: Int,
    @NonNull @ColumnInfo(name = "dosageMg") val dosageMg: Int,
    @NonNull @ColumnInfo(name = "droppedOff") var droppedOff: Boolean = false,
    @NonNull @PrimaryKey val timeTakenMsEpoch: Long
)
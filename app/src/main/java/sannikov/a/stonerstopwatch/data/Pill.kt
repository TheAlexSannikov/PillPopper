package sannikov.a.stonerstopwatch.data

import androidx.annotation.NonNull
import androidx.room.*

/**
 * Stores information for an individual pill taken by the user. Storable in Room
 */
@Entity
data class Pill(
    @NonNull @ColumnInfo val drug: Drug,
    @NonNull @ColumnInfo val dosageMg: Int,
    @NonNull @PrimaryKey val timeTakenMsEpoch: Long
)
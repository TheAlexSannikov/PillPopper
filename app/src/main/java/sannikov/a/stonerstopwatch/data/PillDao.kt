package sannikov.a.stonerstopwatch.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PillDao {
    @Query("SELECT * from pill")
    fun loadAll(): Flow<List<Pill>>

    @Query("SELECT * FROM pill WHERE drug = :qDrug")
    fun loadAllByDrug(qDrug: Drug): Flow<List<Pill>>

    @Insert
    suspend fun popPill(pill : Pill)

    @Query("SELECT * FROM pill ORDER BY timeTakenMsEpoch DESC LIMIT 1")
    suspend fun getMostRecentPill() : Pill

    @Delete
    suspend fun deletePill(pill: Pill)

    @Query("DELETE FROM pill")
    suspend fun deleteAllPills()

    @Query("SELECT SUM(dosageMg) FROM Pill WHERE drug = :qDrug")
    suspend fun getAmountConsumedMg(qDrug: Drug): Int?

    @Query("SELECT * FROM Pill WHERE timeTakenMsEpoch = :timestamp" )
    suspend fun queryPillByTimestamp(timestamp: Long): Pill

    @Update
    suspend fun updatePill(pill: Pill)
}
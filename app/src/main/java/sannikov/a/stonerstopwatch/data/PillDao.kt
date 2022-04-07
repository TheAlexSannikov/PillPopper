package sannikov.a.stonerstopwatch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PillDao {
    @Query("SELECT * from pill")
    fun loadAll(): Flow<List<Pill>>

    @Query("SELECT * FROM pill WHERE drug = :qDrug")
    fun loadAllByDrug(qDrug: Drug): Flow<List<Pill>>

    @Insert
    suspend fun popPill(pill : Pill)

    @Query("DELETE FROM pill")
    fun deleteAllPills()
}
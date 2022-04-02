package sannikov.a.stonerstopwatch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PillDao {
    @Query("SELECT * from pill")
    fun getAll(): List<Pill>

    @Query("SELECT * FROM pill WHERE drug = :qDrug")
    fun loadAllByDrug(qDrug: Drug): List<Pill>

    @Insert
    suspend fun popPill(pill : Pill)
}
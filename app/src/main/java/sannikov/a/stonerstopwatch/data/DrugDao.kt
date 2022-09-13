package sannikov.a.stonerstopwatch.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * The DAO that stores information on Drugs (which are mutable)
 */
@Dao
interface DrugDao {

    @Query("SELECT * from Drug")
    fun loadAll(): Flow<List<Drug>>

    @Insert
    suspend fun addDrug(drug : Drug)

    @Query("SELECT * FROM Drug WHERE drugId = :drugId LIMIT 1")
    suspend fun loadByDrugId(drugId: Int): Drug
}
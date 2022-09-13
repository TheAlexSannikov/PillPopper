package sannikov.a.stonerstopwatch.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrugRepository @Inject constructor(
    private val drugDao: DrugDao
) {
    fun loadAll(): Flow<List<Drug>> {
        return drugDao.loadAll()
    }

    suspend fun addDrug(drug: Drug) {
        drugDao.addDrug(drug)
    }

    suspend fun loadByDrugId(drugId: Int):Drug {
        return drugDao.loadByDrugId(drugId = drugId)
    }
}
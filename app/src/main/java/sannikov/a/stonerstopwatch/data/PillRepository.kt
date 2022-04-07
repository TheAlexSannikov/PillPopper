package sannikov.a.stonerstopwatch.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PillRepository @Inject constructor(
    private val pillDao : PillDao
){
    suspend fun loadAll(): Flow<List<Pill>> {
        return pillDao.loadAll()
    }

    suspend fun loadAllByDrug(drug: Drug): Flow<List<Pill>> {
        return pillDao.loadAllByDrug(qDrug = drug)
    }

    suspend fun popPill(pill : Pill) {
        pillDao.popPill(pill)
    }

    fun deleteAllPills() {
        pillDao.deleteAllPills()
    }
}
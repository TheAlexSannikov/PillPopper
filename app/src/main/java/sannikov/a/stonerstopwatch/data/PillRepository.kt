package sannikov.a.stonerstopwatch.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PillRepository @Inject constructor(
    private val pillDao: PillDao
) {
    suspend fun loadAll(): Flow<List<Pill>> {
        return pillDao.loadAll()
    }

    suspend fun loadAllByDrug(drug: Drug): Flow<List<Pill>> {
        return pillDao.loadAllByDrug(qDrug = drug)
    }

    /**
     * pop as in ingesting it
     */
    suspend fun popPill(pill: Pill) {
        pillDao.popPill(pill)
    }

    suspend fun getMostRecentPill(): Pill {
        return pillDao.getMostRecentPill()
    }

    suspend fun deletePill(pill: Pill) {
        pillDao.deletePill(pill)
    }

    suspend fun deleteAllPills() {
        pillDao.deleteAllPills()
    }

    suspend fun getAmountConsumedMg(drug: Drug): Int {
        val daoRet = pillDao.getAmountConsumedMg(qDrug = drug)
        return if(daoRet != null) daoRet else 0
    }

    suspend fun queryPillByTimestamp(timestamp: Long): Pill {
        return pillDao.queryPillByTimestamp(timestamp)
    }

    suspend fun updatePill(pill: Pill) {
        return pillDao.updatePill(pill)
    }
}
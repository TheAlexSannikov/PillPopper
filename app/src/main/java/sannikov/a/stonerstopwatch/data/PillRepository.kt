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

    /**
     * returns results from [getAmountConsumedMg, getAmountConsumedPreDropOffMg]
     */
    suspend fun getAmountsConsumedMg(drug: Drug): Array<Int> {
        val getAmountConsumedMg = pillDao.getAmountConsumedMg(qDrug = drug) ?: 0
        val getAmountConsumedPreDropOffMg = pillDao.getAmountConsumedPreDropOffMg(qDrug = drug) ?: 0

        return arrayOf(getAmountConsumedMg,getAmountConsumedPreDropOffMg)
    }

    suspend fun getAmountConsumedMg(drug: Drug): Int {
        val daoRet = pillDao.getAmountConsumedMg(qDrug = drug)
        return if(daoRet != null) daoRet else 0
    }

    suspend fun getAmountConsumedPreDropOffMg(drug: Drug): Int {
        val daoRet = pillDao.getAmountConsumedPreDropOffMg(qDrug = drug)
        return if(daoRet != null) daoRet else 0
    }

    suspend fun queryPillByTimestamp(timestamp: Long): Pill {
        return pillDao.queryPillByTimestamp(timestamp)
    }

    suspend fun updatePill(pill: Pill) {
        return pillDao.updatePill(pill)
    }

    /**
     * returns true if the user can pop a certain drug
     */
    suspend fun poppingEnabled(drug: Drug): Boolean {
        val amountConsumed24hrPrior = getAmountConsumedMg(drug)
        val amountConsumedPreDropOffPrior = getAmountConsumedPreDropOffMg(drug)

        return amountConsumed24hrPrior < drug.maxDosageMgPerDay && amountConsumedPreDropOffPrior < drug.maxDosageMgPerPeriod
    }

    /**
     * updates the pill and returns true if the dropOff of pill enables user to take more
     */
    suspend fun updatePillAndCheckIfUpdateEnablePopping(pill: Pill): Boolean {
        val allowedToPopBefore = poppingEnabled(pill.drug)
        updatePill(pill)
        return (!allowedToPopBefore && poppingEnabled(pill.drug))

    }

    /**
     * deletes the pill and returns true if the dropOff of pill enables user to take more
     */
    suspend fun deletePillAndCheckIfUpdateEnablePopping(pill: Pill): Boolean {
        val allowedToPopBefore = poppingEnabled(pill.drug)
        deletePill(pill)
        return (!allowedToPopBefore && poppingEnabled(pill.drug))

    }

}
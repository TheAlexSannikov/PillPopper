package sannikov.a.stonerstopwatch.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PillRepository @Inject constructor(
    private val pillDao: PillDao
) {
    private val TAG = "PillRepository"

    fun loadAll(): Flow<List<Pill>> {
        return pillDao.loadAll()
    }

    fun loadAllByDrugId(drug: Drug): Flow<List<Pill>> {
        return pillDao.loadAllByDrugId(drugId = drug.drugId)
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
        val getAmountConsumedMg = pillDao.getAmountConsumedMg(drugId = drug.drugId) ?: 0
        val getAmountConsumedPreDropOffMg = pillDao.getAmountConsumedPreDropOffMg(drugId = drug.drugId) ?: 0

        return arrayOf(getAmountConsumedMg,getAmountConsumedPreDropOffMg)
    }

    suspend fun getAmountConsumedMg(drug: Drug): Int {
        return pillDao.getAmountConsumedMg(drugId = drug.drugId) ?: 0
    }

    suspend fun getAmountConsumedPreDropOffMg(drug: Drug): Int {
        return pillDao.getAmountConsumedPreDropOffMg(drugId = drug.drugId) ?: 0
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

        val amountConsumed24hr = getAmountConsumedMg(drug)
        val amountConsumedPreDropOff = getAmountConsumedPreDropOffMg(drug)

        val ret = (amountConsumed24hr + drug.dosageMg) <= drug.maxDosageMgPerDay && (amountConsumedPreDropOff + drug.dosageMg) <= drug.maxDosageMgPerPeriod

        Log.d(TAG, "poppingEnabled returning $ret")
        return ret
    }
}
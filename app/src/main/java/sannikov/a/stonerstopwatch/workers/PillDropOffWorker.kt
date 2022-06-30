package sannikov.a.stonerstopwatch.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import sannikov.a.stonerstopwatch.data.*

/**
 * handles ONLY pills 'dropping off' after Drug.periodHrs expires
 *
 *  Idea: Trigger PillDropOffWorker when pill is taken (from PillRepository.getMostRecentPill's flow)
 *      Check if worker for this pill already exists (ie on app startup?)
 *
 */
@HiltWorker
class PillDropOffWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val pillRepository: PillRepository
) : CoroutineWorker(appContext, workerParams) {

    private val TAG = "PillDropOffWorker"

    override suspend fun doWork(): Result {
        Log.d(TAG, "doWork; workParams: ${inputData.keyValueMap}")
        val appContext = applicationContext

        val pillTimestamp = inputData.getLong(WorkerParams.TIMESTAMP_TAKEN, 0)
        val workType = inputData.getString(WorkerParams.WORK_TYPE)

        return when(workType) {
            WorkerParams.WORK_TYPE_DROP_OFF -> {
                val pill = pillRepository.queryPillByTimestamp(pillTimestamp)
                if(pill.droppedOff) {
                    Log.e(TAG, "attempting to drop off an already dropped off pill!")
                }
                pill.droppedOff = true
                pillRepository.updatePill(pill)
                Result.success()
            }
            WorkerParams.WORK_TYPE_DROP_OFF_24H -> {
                val pill = pillRepository.queryPillByTimestamp(pillTimestamp)
                pillRepository.deletePill(pill)
                Result.success()
            }
            else -> Result.failure()
        }
    }

    // TODO: Should this be an object, or in companion?
    object WorkerParams {
        const val TIMESTAMP_TAKEN = "tsTaken" // used to lookup pill

        const val WORK_TYPE = "workType"
        const val WORK_TYPE_DROP_OFF = "workTypeDropOff"
        const val WORK_TYPE_DROP_OFF_24H = "workTypeDropOff24h"
    }

        companion object {

        fun createDropOffInputData(pill: Pill): Data {
            val tsTaken = pill.timeTakenMsEpoch
            return Data.Builder()
                .putLong(WorkerParams.TIMESTAMP_TAKEN, tsTaken)
                .putString(WorkerParams.WORK_TYPE, WorkerParams.WORK_TYPE_DROP_OFF)
                .build()
        }

        fun createDropOff24hInputData(pill: Pill): Data {
            val tsTaken = pill.timeTakenMsEpoch
            return Data.Builder()
                .putLong(WorkerParams.TIMESTAMP_TAKEN, tsTaken)
                .putString(WorkerParams.WORK_TYPE, WorkerParams.WORK_TYPE_DROP_OFF_24H)
                .build()
        }
    }
}
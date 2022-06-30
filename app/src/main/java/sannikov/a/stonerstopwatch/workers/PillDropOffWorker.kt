package sannikov.a.stonerstopwatch.workers

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import sannikov.a.stonerstopwatch.data.AppDatabase
import sannikov.a.stonerstopwatch.data.DataStoreManager
import sannikov.a.stonerstopwatch.data.Pill
import sannikov.a.stonerstopwatch.data.PillDao
import java.lang.Exception

/**
 * handles ONLY pills 'dropping off' after Drug.periodHrs expires
 *
 *  Idea: Trigger PillDropOffWorker when pill is taken (from PillRepository.getMostRecentPill's flow)
 *      Check if worker for this pill already exists (ie on app startup?)
 *
 */
class PillDropOffWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    private val TAG = "PillDropOffWorker"

    override fun doWork(): Result {
        Log.d(TAG, "doWork; workParams: ${inputData.keyValueMap}")
        val appContext = applicationContext


        return Result.success()
    }

    // TODO: Should this be an object?
    object WorkerParams {
        const val TIMESTAMP_TAKEN = "tsTaken" // used to lookup pill
        const val TIMESTAMP_DROP_OFF = "tsDropOff"
        const val TIMESTAMP_24H = "ts24Hr"
    }

    companion object {
        val MS_PER_HOUR = 3600000L
        fun createInputData(pill: Pill): Data {
            val tsTaken = pill.timeTakenMsEpoch
            val tsDropOff = tsTaken + pill.drug.periodHrs * MS_PER_HOUR
            return Data.Builder()
                .putLong(WorkerParams.TIMESTAMP_TAKEN, tsTaken)
                .putLong(WorkerParams.TIMESTAMP_DROP_OFF, tsDropOff)
                .build()
        }
    }

    // handles deleting pills after 24hrs of taking a pill.
    class Pill24hDropOffWorker(appContext: Context, workerParams: WorkerParameters) :
        Worker(appContext, workerParams) {

        private val TAG = "Pill24hDropOffWorker"

        override fun doWork(): Result {
            Log.d(TAG, "doWork; workParams: ${inputData.keyValueMap}")
            val appContext = applicationContext

            return try {
                Result.success()
            } catch (e: Exception) {
                Result.failure()
            }
        }

        companion object {
            val MS_PER_DAY = 86400000L

            fun createInputData(pill: Pill): Data {
                val tsTaken = pill.timeTakenMsEpoch
                val ts24Hr = tsTaken + MS_PER_DAY
                return Data.Builder()
                    .putLong(WorkerParams.TIMESTAMP_TAKEN, tsTaken)
                    .putLong(WorkerParams.TIMESTAMP_24H, ts24Hr)
                    .build()
            }
        }
    }
}

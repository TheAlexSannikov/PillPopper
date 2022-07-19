package sannikov.a.stonerstopwatch.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.getSystemService
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import sannikov.a.stonerstopwatch.R
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
//        val appContext = applicationContext // this is a thing

        val pillTimestamp = inputData.getLong(WorkerParams.TIMESTAMP_TAKEN, 0)
        val workType = inputData.getString(WorkerParams.WORK_TYPE)

        return when (workType) {
            WorkerParams.WORK_TYPE_DROP_OFF -> {
                val pill = pillRepository.queryPillByTimestamp(pillTimestamp)
                if (pill.droppedOff) {
                    Log.e(TAG, "attempting to drop off an already dropped off pill!")
                }
                pill.droppedOff = true
                pillRepository.updatePill(pill)
                showNotificationPP()
                Result.success()
            }
            WorkerParams.WORK_TYPE_DROP_OFF_24H -> {
                val pill = pillRepository.queryPillByTimestamp(pillTimestamp)
                pillRepository.deletePill(pill)
                pillRepository.updatePill(pill)
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
        val CHANNEL_NAME_PILL_POPPER = "PillPopper notifications"
        val CHANNEL_ID_PILL_POPPER = "chPP"
        val CHANNEL_DESCRIPTION_PILL_POPPER = "Inform of when it\'s safe to take next pills"

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

    private fun showNotificationPP() {
        val channelId = CHANNEL_ID_PILL_POPPER
        val title = "time to pop pills!"
        val text = "its been 8 hours since you've last popped one"

        var builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_acetaminophen)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        createNotificationChannelPP(notificationManager)

        Log.d(TAG, "showNotificationPP: notifying!")
        notificationManager.notify(0, builder.build())
    }

    private fun createNotificationChannelPP(notificationManager: NotificationManagerCompat) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = CHANNEL_ID_PILL_POPPER
            val name = CHANNEL_NAME_PILL_POPPER
            val descriptionText = CHANNEL_DESCRIPTION_PILL_POPPER
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(id, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            Log.d(TAG, "createNotificationChannelPP: creating channel!")
            notificationManager.createNotificationChannel(channel)
        }
    }
}
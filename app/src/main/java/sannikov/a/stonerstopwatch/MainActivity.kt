package sannikov.a.stonerstopwatch

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import sannikov.a.stonerstopwatch.data.AppDatabase
import sannikov.a.stonerstopwatch.data.DataStoreManager
import sannikov.a.stonerstopwatch.viewmodels.StopwatchViewModel
import sannikov.a.stonerstopwatch.viewmodels.StopwatchStates

import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "MainActivity"
    }

    @Inject
    lateinit var dataStoreManager: DataStoreManager
    private val stopwatchViewModel: StopwatchViewModel by viewModels()
    @Inject
    lateinit var appDatabase: AppDatabase
    private val timeBetweenTicksMs = 10L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startClock()

        setContent {
            MaterialTheme() {
                MainScreenBottomNav()
            }
        }
    }

    /*
    private fun showNotificationPP() {
        val channelId = getString(R.string.channel_id_pill_popper)
        val title = "time to pop pills!"
        val text = "its been 8 hours since you've last popped one"

        var builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_ibuprofen)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        createNotificationChannelPP()

        with(NotificationManagerCompat.from(this)) {
            notify(0, builder.build())
        }

    }

    private fun createNotificationChannelPP() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val id = getString(R.string.channel_id_pill_popper)
            val name = getString(R.string.channel_name_pill_popper)
            val descriptionText = getString(R.string.channel_desciption_pill_popper)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(id, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
     */

    private fun startClock() {
        // TODO: block when the state is not running
        var delayCount = 0
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    // block when the stopwatch is not RUNNING
                    if (stopwatchViewModel.stopwatchState.value != StopwatchStates.RUNNING) {
                        Log.d(TAG, "startClock, sleeping on stopwatchState. stateViewModel: $stopwatchViewModel")
                        stopwatchViewModel.stopwatchState.first { newState -> newState == StopwatchStates.RUNNING }
                    }
                    delay(timeBetweenTicksMs)
                    if (delayCount % 15000 == 0) {
                        Log.d(TAG, "startTicks, delayCount: $delayCount")
                        Log.d(TAG, "Notifying")
//                        showNotificationPP()
                    }
                    delayCount++
                    stopwatchViewModel.onClockChange(newClock = System.currentTimeMillis(), print = false)
                }
            }
        }
    }

    public override fun onStop() {
        super.onStop()
        stopwatchViewModel.saveState()
    }
}


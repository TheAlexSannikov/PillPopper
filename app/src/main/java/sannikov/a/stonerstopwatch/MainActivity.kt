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


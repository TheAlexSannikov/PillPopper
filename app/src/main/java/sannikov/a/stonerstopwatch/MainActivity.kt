package sannikov.a.stonerstopwatch

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
//import sannikov.a.stonerstopwatch.ui.theme.StonerStopwatchTheme
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class MainActivity : ComponentActivity() {
    private val tag = "MainActivity"

    lateinit var dataStoreManager: DataStoreManager
    lateinit var stateViewModel: StateViewModel
    val timeBetweenTicksMs = 10L



    @ExperimentalGraphicsApi
    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.Theme_StonerStopwatch)
        super.onCreate(savedInstanceState)

        dataStoreManager = DataStoreManager.getInstance(this@MainActivity)
        stateViewModel =
            StateViewModel(dataStoreManager = dataStoreManager) // to interact with MutableLiveData (now StateFlow)
        startClock()

        setContent {
            MaterialTheme() {
                MainScreenBottomNav(stateViewModel = stateViewModel)
            }
        }
    }

    fun startClock() {
        // TODO: block when the state is not running
        var delayCount = 0
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    // block when the stopwatch is not RUNNING
                    if (stateViewModel.stopwatchState.value != StopwatchStates.RUNNING) {
                        Log.d(tag, "startClock, sleeping on stopwatchState")
                        stateViewModel.stopwatchState.first { newState -> newState == StopwatchStates.RUNNING }
                    }
                    delay(timeBetweenTicksMs)
                    if (delayCount % 500 == 0) {
                        Log.d(tag, "startTicks, delayCount: $delayCount")
                    }
                    delayCount++
                    stateViewModel.onClockChange(System.currentTimeMillis())
                }
            }
        }
    }

    public override fun onStop() {
        super.onStop()
        dataStoreManager.saveState(stateViewModel)
    }
}


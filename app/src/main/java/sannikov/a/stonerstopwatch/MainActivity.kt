package sannikov.a.stonerstopwatch

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
//import sannikov.a.stonerstopwatch.ui.theme.StonerStopwatchTheme
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"

    @Inject
//    @Named("dataStoreManager")
    lateinit var dataStoreManager: DataStoreManager

    @Inject
//    @Named("stateViewModel") // I guess this annotation causes issues when using hiltViewModel() within a composable?
    lateinit var stateViewModel: StateViewModel

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
                    if (stateViewModel.stopwatchState.value != StopwatchStates.RUNNING) {
                        Log.d(tag, "startClock, sleeping on stopwatchState. stateViewModel: $stateViewModel")
                        stateViewModel.stopwatchState.first { newState -> newState == StopwatchStates.RUNNING }
                    }
                    delay(timeBetweenTicksMs)
                    if (delayCount % 500 == 0) {
                        Log.d(tag, "startTicks, delayCount: $delayCount")
                    }
                    delayCount++
                    stateViewModel.onClockChange(newClock = System.currentTimeMillis(), print = false)
                }
            }
        }
    }

    public override fun onStop() {
        super.onStop()
        stateViewModel.saveState()
    }
}


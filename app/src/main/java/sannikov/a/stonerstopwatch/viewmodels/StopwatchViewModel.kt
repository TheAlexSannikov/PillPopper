package sannikov.a.stonerstopwatch.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sannikov.a.stonerstopwatch.data.DataStoreManager
import sannikov.a.stonerstopwatch.data.StopwatchStates
import sannikov.a.stonerstopwatch.views.STOPWATCH_PERIOD_MS
import javax.inject.Inject
import javax.inject.Named


//class StateViewModel(dataStoreManager: DataStoreManager? = null) : ViewModel() {
@HiltViewModel
class StopwatchViewModel @Inject constructor(@Named("dataStoreManager") private val dataStoreManager: DataStoreManager) :
    ViewModel() {
    private val TAG = "StateViewModel"

    private val _stopwatchState = MutableStateFlow<StopwatchStates>(StopwatchStates.PAUSED)

    val stopwatchState: StateFlow<StopwatchStates> = _stopwatchState.asStateFlow()

    fun onStopwatchStateChange(newState: StopwatchStates) {
        Log.d(TAG, "newState: $newState")
        val currTime = System.currentTimeMillis()

        when(newState) {
            StopwatchStates.RUNNING -> {
                val pauseTimestampMs = pauseTimestampMs.value
                // account for time the timer was paused moving by startTime forward by that amount of time
                val lengthOfPause = currTime - pauseTimestampMs;

                Log.d(TAG, "\tlengthOfPause was known to be $lengthOfPause")
                Log.d(TAG, "\tnewStartTimestampMs = ${startTimestampMs.value + lengthOfPause}")
                onStartTimestampMsChange(newStartTimestampMs = startTimestampMs.value + lengthOfPause)
            }
            StopwatchStates.PAUSED -> {
                Log.d(TAG, "\tnewPauseTimestampMs = $currTime")
                onPauseTimestampMsChange(newPauseTimestampMs = currTime)
            }
            StopwatchStates.RESET -> {
                Log.d(TAG, "\tnewStartTimestampMs = $currTime, newPauseTimestampMs = $currTime, newClock = $currTime")
                onStartTimestampMsChange(newStartTimestampMs = currTime)
                onPauseTimestampMsChange(newPauseTimestampMs = currTime)
                onClockChange(newClock = currTime, print = true)
            }
        }

        _stopwatchState.value = newState
    }


    private val _startTimestampMs: MutableStateFlow<Long> = MutableStateFlow(STOPWATCH_PERIOD_MS)

    val startTimestampMs: StateFlow<Long> = _startTimestampMs.asStateFlow()

    fun onStartTimestampMsChange(newStartTimestampMs: Long) {
        Log.d(TAG, "newStartTimestampMs: " + newStartTimestampMs)
        _startTimestampMs.value = newStartTimestampMs
    }

    private val _pauseTimestampMs = MutableStateFlow(System.currentTimeMillis())

    val pauseTimestampMs: StateFlow<Long> = _pauseTimestampMs.asStateFlow()

    fun onPauseTimestampMsChange(newPauseTimestampMs: Long) {
        Log.d(TAG, "newPauseTimestampMs: $newPauseTimestampMs")
        _pauseTimestampMs.value = newPauseTimestampMs
    }

    private val _clock = MutableStateFlow<Long>(System.currentTimeMillis())

    val clock: StateFlow<Long> = _clock.asStateFlow()

    fun onClockChange(newClock: Long, print: Boolean? = null) {
//        val ssv = startTimestampMs.value
//        if(print == true) {
//            Log.d(tag, "startTimestampMs: $ssv\t\t\tstop newClock: $newClock")
//        }
        _clock.value = newClock
        // TODO: Refactor so display is updated in a flow
        val elapsedTimeMs = when (stopwatchState.value) {
            StopwatchStates.RUNNING -> {
                newClock - startTimestampMs.value
            }
            StopwatchStates.PAUSED -> {
                pauseTimestampMs.value - startTimestampMs.value
            }
            StopwatchStates.RESET -> {
                0
            }
        }

        onElapsedTimeChange(elapsedTimeMs)
    }

    private val _elapsedTimeMs = MutableStateFlow(
        value = when (stopwatchState.value) {
            StopwatchStates.RUNNING -> {
                System.currentTimeMillis() - startTimestampMs.value
            }
            StopwatchStates.PAUSED -> {
                pauseTimestampMs.value - startTimestampMs.value
            }
            StopwatchStates.RESET -> {
                0
            }
        }
    )

    val elapsedTimeMs: StateFlow<Long> = _elapsedTimeMs.asStateFlow()

    fun onElapsedTimeChange(newElapsedTimeMs: Long) {
//        Log.d(tag, "newDisplayTime: $newDisplayTime")
        _elapsedTimeMs.value = newElapsedTimeMs
    }

    fun onPressStartStop() {
        onStopwatchStateChange(when(stopwatchState.value) {
            StopwatchStates.RUNNING -> StopwatchStates.PAUSED
            else -> StopwatchStates.RUNNING
        })
    }

    fun onPressReset() {
        onStopwatchStateChange(StopwatchStates.RESET)
    }

    init {
        Log.d(TAG, "init!")
        viewModelScope.launch {
            // TODO: may want to put back in null checks:   dataStoreManager?.read
            val newState = dataStoreManager.read("stopwatchState")
            val newpauseTimestampMs = dataStoreManager.read("pauseTimestampMs")
            val newstartTimestampMs = dataStoreManager.read("startTimestampMs")
            newState?.let { onStopwatchStateChange(newState as StopwatchStates) }
            newpauseTimestampMs?.let { onPauseTimestampMsChange(newpauseTimestampMs as Long) }
            newstartTimestampMs?.let { onStartTimestampMsChange(newstartTimestampMs as Long) }

            Log.d(
                TAG,
                "init, state: $newState, pauseTimestampMs: $newpauseTimestampMs, startTimestampMs: $newstartTimestampMs"
            )
            onClockChange(System.currentTimeMillis()) // hacky way to fix bug where incorrect time when launched and paused.
        }
    }

    // saves all values to persistent storage
    fun saveState() {
        Log.d(TAG, "saveState:")
        GlobalScope.launch(Dispatchers.IO) {
            dataStoreManager.save(
                "stopwatchState",
                this@StopwatchViewModel.stopwatchState.value.ordinal
            )
            dataStoreManager.save("pauseTimestampMs", this@StopwatchViewModel.pauseTimestampMs.value)
            dataStoreManager.save("startTimestampMs", this@StopwatchViewModel.startTimestampMs.value)
        }
    }

}
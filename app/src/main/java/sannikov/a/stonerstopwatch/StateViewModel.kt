package sannikov.a.stonerstopwatch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


enum class StopwatchStates {
    RESET,
    RUNNING,
    PAUSED,
}

class StateViewModel(dataStoreManager: DataStoreManager) : ViewModel() {
    private val tag = "StateViewModel"

    private val _stopwatchState = MutableStateFlow<StopwatchStates>(StopwatchStates.RUNNING)

    val stopwatchState: StateFlow<StopwatchStates> = _stopwatchState.asStateFlow()

    fun onStopwatchStateChange(newState: StopwatchStates) {
        Log.d(tag, "newState: " + newState)
        _stopwatchState.value = newState
    }


    private val _startTimestampMs: MutableStateFlow<Long> = MutableStateFlow(denominatorTime)

    val startTimestampMs: StateFlow<Long> = _startTimestampMs.asStateFlow()

    fun onStartTimestampMsChange(newStartTimestampMs: Long) {
        _startTimestampMs.value = newStartTimestampMs
    }

    private val _pauseTimestampMs = MutableStateFlow(System.currentTimeMillis())

    val pauseTimestampMs: StateFlow<Long> = _pauseTimestampMs.asStateFlow()

    fun onPauseTimestampMsChange(newPauseTimestampMs: Long) {
        Log.d(tag, "newPauseTimestampMs: " + newPauseTimestampMs)
        _pauseTimestampMs.value = newPauseTimestampMs
    }

    private val _clock = MutableStateFlow<Boolean>(false)

    val clock: StateFlow<Boolean> = _clock.asStateFlow()

    fun onClockChange(newClock: Boolean) {
        Log.d(tag, "newClock: $newClock")
        _clock.value = newClock
    }

    init {
        viewModelScope.launch {
            val newState = dataStoreManager.read("stopwatchState")
            val newpauseTimestampMs = dataStoreManager.read("pauseTimestampMs")
            val newstartTimestampMs = dataStoreManager.read("startTimestampMs")

            Log.d(tag, "init, state: $newState, TimestampMs: $newpauseTimestampMs, startTimestampMs: $newstartTimestampMs")

            newState?.let { onStopwatchStateChange(newState as StopwatchStates) }
            newpauseTimestampMs?.let { onPauseTimestampMsChange(newpauseTimestampMs as Long) }
            newstartTimestampMs?.let { onStartTimestampMsChange(newstartTimestampMs as Long)}

        }
    }

}
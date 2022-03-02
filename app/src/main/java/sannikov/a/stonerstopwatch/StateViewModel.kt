package sannikov.a.stonerstopwatch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


enum class StopwatchStates {
    RESET,
    RUNNING,
    PAUSED,
}

class StateViewModel(dataStoreManager: DataStoreManager) : ViewModel() {
    private val tag = "StateViewModel"

    private val _stopwatchState = MutableLiveData(StopwatchStates.RUNNING)

    val stopwatchState: LiveData<StopwatchStates> = _stopwatchState

    fun onStopwatchStateChange(newState: StopwatchStates) {
        Log.d(tag, "newState: " + newState)
        _stopwatchState.value = newState
    }


    private val _startTimestampMs: MutableLiveData<Long> = MutableLiveData(denominatorTime)

    val startTimestampMs: LiveData<Long> = _startTimestampMs

    fun onStartTimestampMsChange(newStartTimestampMs: Long) {
        _startTimestampMs.value = newStartTimestampMs
    }

    private val _pauseTimestampMs = MutableLiveData(System.currentTimeMillis())

    val pauseTimestampMs: LiveData<Long> = _pauseTimestampMs

    fun onPauseTimestampMsChange(newPauseTimestampMs: Long) {
        Log.d(tag, "newPauseTimestampMs: " + newPauseTimestampMs)
        _pauseTimestampMs.value = newPauseTimestampMs
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
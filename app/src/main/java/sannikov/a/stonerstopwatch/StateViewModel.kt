package sannikov.a.stonerstopwatch

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey


enum class StopwatchStates {
    RESET,
    RUNNING,
    PAUSED,
}

//sealed class DataStoreKeys2(
//    val key: Preferences.Key<Any>,
//    val keyName: String,
//    val saveCodeInt: Int,
//) {
//    object Reset: stopwatchStates(
//        key = (intPreferencesKey("STOPWATCH_STATE") as Preferences.Key<Any>),
//        keyName = ""
//    )
//
//}

class StateViewModel(dataStoreManager: DataStoreManager) : ViewModel() {
    private val tag = "StateViewModel"
    private val _startTimestamp: MutableLiveData<Long> = MutableLiveData(startTimeMs)
    private val _stopwatchState = MutableLiveData(StopwatchStates.RUNNING)

    init {
        viewModelScope.launch {
            val newState = dataStoreManager.read()
            Log.d(tag, "init, newState: " + newState)
            newState?.let { onStopwatchStateChange(newState as StopwatchStates) }
        }
    }

    val startTimestamp: LiveData<Long> = _startTimestamp
    val stopwatchState: LiveData<StopwatchStates> = _stopwatchState


    fun onStopwatchStateChange(newState: StopwatchStates) {
        Log.d(tag, "newState: " + newState)
        _stopwatchState.value = newState
    }

    fun onStartTimestampChange(newStartTimestamp: Long) {
        _startTimestamp.value = newStartTimestamp
    }

    // todo: remove below variables
    private val _currentTime: MutableLiveData<Long> = MutableLiveData()
    val currentTime: LiveData<Long> = _currentTime

    fun onCurrentTimeChange(newCurrentTime: Long) {
        _currentTime.value = newCurrentTime
    }
}
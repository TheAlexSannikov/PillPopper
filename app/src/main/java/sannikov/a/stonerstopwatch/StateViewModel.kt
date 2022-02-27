package sannikov.a.stonerstopwatch

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StateViewModel(dataStoreManager: DataStoreManager) : ViewModel() {
    private val tag = "StateViewModel"
    private val _startTimestamp: MutableLiveData<Long> = MutableLiveData(startTimeMs)
    private val _isRunning = MutableLiveData(true)
    init {
        viewModelScope.launch {
            val newIsRunning = dataStoreManager.readFromDataStore()
            Log.d(tag, "newIsRunning: " + newIsRunning)
            newIsRunning?.let { onIsRunningChange(newIsRunning) }
        }
    }

    val startTimestamp: LiveData<Long> = _startTimestamp
    val isRunning: LiveData<Boolean> = _isRunning


    fun onIsRunningChange(newIsRunning: Boolean) {
        _isRunning.value = newIsRunning
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
package sannikov.a.stonerstopwatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StateViewModel : ViewModel() {
    private val _startTimestamp : MutableLiveData<Long> = MutableLiveData(0)
    private val _isRunning : MutableLiveData<Boolean> = MutableLiveData(false)
    val startTimestamp: LiveData<Long> = _startTimestamp
    val isRunning: LiveData<Boolean> = _isRunning

    fun onIsRunningChange(newIsRunning: Boolean){
        _isRunning.value = newIsRunning
    }
    fun onStartTimestampChange(newStartTimestamp: Long){
        _startTimestamp.value = newStartTimestamp
    }

    // todo: remove below variables
    private val _currentTime : MutableLiveData<Long> = MutableLiveData(10000L)
    val currentTime: LiveData<Long> = _currentTime

    fun onCurrentTimeChange(newCurrentTime: Long){
        _currentTime.value = newCurrentTime
    }
}
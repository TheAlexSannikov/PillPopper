package sannikov.a.stonerstopwatch.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sannikov.a.stonerstopwatch.data.Pill
import sannikov.a.stonerstopwatch.data.PillRepository
import javax.inject.Inject

@HiltViewModel
class PillViewModel @Inject constructor(
    private val pillRepository: PillRepository
) : ViewModel() {

    private val TAG = "PillViewModel"

    private val _allPoppedPills = MutableStateFlow<List<Pill>>(value = emptyList())
    val allPoppedPills: StateFlow<List<Pill>> = _allPoppedPills.asStateFlow()

    fun onAllPoppedPillsChange(allNewPills: List<Pill>) {
        Log.d(TAG, "onAllPoppedPillsChange: allNewPills: $allNewPills")
        _allPoppedPills.value = allNewPills
    }

    /* My understanding of the data flow:
        pillPopped -> pillRepository -> PillDao [ Room! ]
        After the pill is added to Room, the new list of pills is emitted as a flow.
        pillDao -> pillRepository -> pillRepository.loadAll().collect -> onAllPoppedPillsChange -> updates _allPoppedPills -> triggers allPoppedPills to emit.
     */
    // handles taking a single pill
    fun onPopPill(pill: Pill) {
        Log.d(TAG, "popping pill ${pill}")
        viewModelScope.launch {
            pillRepository.popPill(pill)
        }
    }

    fun deleteAllPills() {
        Log.d(TAG, "deleting all pills!")
        viewModelScope.launch {
            pillRepository.deleteAllPills()
        }
    }

    init {
        Log.d(TAG, "init!")
        viewModelScope.launch {
            pillRepository.loadAll().collect { pills ->
                onAllPoppedPillsChange(pills)
            }
        }
    }
}
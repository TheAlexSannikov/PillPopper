package sannikov.a.stonerstopwatch.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import sannikov.a.stonerstopwatch.data.Pill
import sannikov.a.stonerstopwatch.data.PillRepository
import javax.inject.Inject

@HiltViewModel
class PillViewModel @Inject constructor(
    private val pillRepository: PillRepository
) : ViewModel() {

    private val TAG = "PillViewModel"

    /* My understanding of the data flow:
        pillPopped -> pillRepository -> PillDao [ Room! ]
        After the pill is added to Room, the new list of pills is emitted as a flow.
        pillDao -> pillRepository -> pillRepository.loadAll().collect -> onAllPoppedPillsChange -> updates _allPoppedPills -> triggers allPoppedPills to emit.
     */
    // handles taking a single pill
    fun onPopPill(pill: Pill) {
        Log.d(TAG, "popping pill $pill")
        viewModelScope.launch {
            pillRepository.popPill(pill)
            onCanUndoPillChange(true)
        }
    }

    fun deleteAllPills() {
        Log.d(TAG, "deleting all pills!")
        viewModelScope.launch {
            pillRepository.deleteAllPills()
        }
    }

    // stores the value of next undo
    // can be the most recently popped pill TODO: also enable redo?
    private val _canUndoPill = MutableStateFlow(false)

    val canUndoPill = _canUndoPill.asStateFlow()

    private fun onCanUndoPillChange(value: Boolean) {
        _canUndoPill.value = value
    }

    fun onUndoPill() {
        if (!canUndoPill.value) {
            Log.d(TAG, "onUndoPill is called with canUndoPill: $canUndoPill. Returning")
            return
        }
        viewModelScope.launch {
            val previousPill = pillRepository.getMostRecentPill() as Pill
            Log.d(
                TAG,
                "onUndoPill is called with canUndoPill: $canUndoPill; updated previousPill to $previousPill"
            )
            pillRepository.deletePill(previousPill)
        }
        onCanUndoPillChange(false)
    }
//
//    val _undoable: Flow<Boolean> = undoAction.map {
//        Log.d(TAG, "undoAction $it was captured. undoable is now: ${ it != null}")
//        it != null
//    }
//    val _undoable: StateFlow<Boolean> = MutableStateFlow(false)


    private val _allPoppedPills = MutableStateFlow<List<Pill>>(emptyList())
    val allPoppedPills: StateFlow<List<Pill>> = _allPoppedPills.asStateFlow()

    init {
        /**
         * Only to be updated by changes to the repository!
         * Note: First real-world application of textbook reading was nesting 'onAllPillsChanged' here!
         */
        fun onAllPoppedPillsChange(allNewPills: List<Pill>) {
            Log.d(TAG, "onAllPoppedPillsChange: allNewPills: $allNewPills")
            _allPoppedPills.value = allNewPills
        }

        Log.d(TAG, "init!")
        viewModelScope.launch {
            pillRepository.loadAll().collect { pills ->
                onAllPoppedPillsChange(pills)
            }
        }
    }
}
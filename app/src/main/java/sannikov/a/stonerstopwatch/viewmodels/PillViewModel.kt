package sannikov.a.stonerstopwatch.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import sannikov.a.stonerstopwatch.data.Drug
import sannikov.a.stonerstopwatch.data.Pill
import sannikov.a.stonerstopwatch.data.PillRepository
import javax.inject.Inject

@HiltViewModel
class PillViewModel @Inject constructor(
    private val pillRepository: PillRepository,
    @ApplicationContext val appContext: Context
) : ViewModel() {

    private val TAG = "PillViewModel"

    /* My understanding of the data flow:
        pillPopped -> pillRepository -> PillDao [ Room! ]
        After the pill is added to Room, the new list of pills is emitted as a flow.
        pillDao -> pillRepository -> pillRepository.loadAll().collect -> onAllPoppedPillsChange -> updates _allPoppedPills -> triggers allPoppedPills to emit.
     */
    // handles taking a single pill
    fun onPopPill() {
        val pill = Pill(
            drug = selectedDrug.value,
            dosageMg = 500,
            timeTakenMsEpoch = System.currentTimeMillis()
        )

        Log.d(TAG, "popping pill $pill")
        onCanUndoPillChange(false)
        viewModelScope.launch {
            pillRepository.popPill(pill)
            onCanUndoPillChange(true)
        }
    }

    fun deleteAllPills() {
        Log.d(TAG, "deleting all pills! appContext: $appContext")
        onCanUndoPillChange(false)
        viewModelScope.launch {
            pillRepository.deleteAllPills()
        }
    }

    // stores the value of next undo
    // can be the most recently popped pill TODO: also enable redo?
    private val _canUndoPill = MutableStateFlow(false)

    val canUndoPill = _canUndoPill.asStateFlow()

    private fun onCanUndoPillChange(value: Boolean) {
        Log.d(TAG, "onCanUndoPillChange, value: $value")
        _canUndoPill.value = value
    }

    fun onUndoPill() {
        if (!canUndoPill.value) {
            Log.d(TAG, "onUndoPill is called with canUndoPill: ${canUndoPill.value} Returning")
            return
        }
        viewModelScope.launch {
            val previousPill = pillRepository.getMostRecentPill()
            Log.d(
                TAG,
                "onUndoPill is called with canUndoPill: ${canUndoPill.value}; updated previousPill to $previousPill"
            )
            pillRepository.deletePill(previousPill)
        }
        onCanUndoPillChange(false)
    }

    // the drug the user is about to pop
    private val _selectedDrug = MutableStateFlow(Drug.ACETAMINOPHEN)
    val selectedDrug: StateFlow<Drug> = _selectedDrug.asStateFlow()

    fun onDrugSelectRight() {
        val selectedDrugOrdinal = selectedDrug.value.ordinal
        Log.d(TAG, "onDrugSelectRight, selectedDrugOrdinal: $selectedDrugOrdinal")
        if (selectedDrugOrdinal == Drug.values().size - 1) return
        _selectedDrug.value = Drug.values()[selectedDrugOrdinal + 1]
    }

    fun onDrugSelectLeft() {
        val selectedDrugOrdinal = selectedDrug.value.ordinal
        Log.d(TAG, "onDrugSelectLeft, selectedDrugOrdinal: $selectedDrugOrdinal")
        if (selectedDrugOrdinal == 0) return
        _selectedDrug.value = Drug.values()[selectedDrugOrdinal - 1]
    }

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
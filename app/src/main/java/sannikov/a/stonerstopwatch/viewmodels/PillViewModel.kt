package sannikov.a.stonerstopwatch.viewmodels

import android.content.Context
import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import sannikov.a.stonerstopwatch.data.Drug
import sannikov.a.stonerstopwatch.data.Pill
import sannikov.a.stonerstopwatch.data.PillRepository
import sannikov.a.stonerstopwatch.workers.PillDropOffWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PillViewModel @Inject constructor(
    private val pillRepository: PillRepository,
    @ApplicationContext val appContext: Context
) : ViewModel() {

    private val TAG = "PillViewModel"
    private val workManger: WorkManager = WorkManager.getInstance(appContext)
    private val WORKER_TIME_UNIT = TimeUnit.SECONDS // expected .HOURS, but can be .SECONDS for fast mode

    /* My understanding of the data flow:
        pillPopped -> pillRepository -> PillDao [ Room! ]
        After the pill is added to Room, the new list of pills is emitted as a flow.
        pillDao -> pillRepository -> pillRepository.loadAll().collect -> onAllPoppedPillsChange -> updates _allPoppedPills -> triggers allPoppedPills to emit.
     */
    // handles taking a single pill
    fun onPopPill(scaffoldState: ScaffoldState) {

        val drug = selectedDrug.value
        val pill = Pill(
            drug = drug,
            dosageMg = drug.defaultDosageMg,
            timeTakenMsEpoch = System.currentTimeMillis()
        )

        Log.d(TAG, "popping pill $pill")

        val dropOffData = PillDropOffWorker.createDropOffInputData(pill)
        val dropOffData24hr = PillDropOffWorker.createDropOff24hInputData(pill)

        // initiate the dropOff timers (auto delete after [drug.periodHrs] and 24 hours)
        val dropOffRequest = OneTimeWorkRequestBuilder<PillDropOffWorker>()
            .addTag(pill.timeTakenMsEpoch.toString())
            .setInitialDelay(pill.drug.periodHrs.toLong(), WORKER_TIME_UNIT)
            .setInputData(dropOffData)
            .build()

        val dropOffRequest24h = OneTimeWorkRequestBuilder<PillDropOffWorker>()
            .addTag(pill.timeTakenMsEpoch.toString())
            .setInitialDelay(24L, WORKER_TIME_UNIT)
            .setInputData(dropOffData24hr)
            .build()

        workManger.beginWith(listOf(dropOffRequest, dropOffRequest24h)).enqueue()

        viewModelScope.launch {
            pillRepository.popPill(pill)
            onCanUndoPillChange(true)
            val amountConsumed = getAmountConsumedMg(drug)
            // TODO: Make snackbar useful - its too spammy
//            scaffoldState.snackbarHostState.showSnackbar("popped ${amountConsumed}mg of ${drug.name}", duration = SnackbarDuration.Short)
        }
    }

    fun deleteAllPills() {
        Log.d(TAG, "deleting all pills! appContext: $appContext")
        onCanUndoPillChange(false)
        viewModelScope.launch {
            pillRepository.deleteAllPills()
        }
        workManger.cancelAllWork()
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
            workManger.cancelAllWorkByTag(previousPill.timeTakenMsEpoch.toString())
        }
        onCanUndoPillChange(false)
    }

    // the drug the user is about to pop
    private val _selectedDrug = MutableStateFlow(Drug.ACETAMINOPHEN)
    val selectedDrug: StateFlow<Drug> = _selectedDrug.asStateFlow()

    fun onDrugSelectRight() {
        val selectedDrugOrdinal = selectedDrug.value.ordinal
        Log.d(TAG, "onDrugSelectRight, selectedDrugOrdinal: $selectedDrugOrdinal")
        if (isDrugToRight()) {
            onSelectedDrugChange(Drug.values()[selectedDrugOrdinal + 1])
        }
    }

    fun isDrugToRight(): Boolean {
        val selectedDrugOrdinal = selectedDrug.value.ordinal
        if (selectedDrugOrdinal == Drug.values().size - 1) {
            return false
        }
        return true
    }

    fun onDrugSelectLeft() {
        val selectedDrugOrdinal = selectedDrug.value.ordinal
        Log.d(TAG, "onDrugSelectLeft, selectedDrugOrdinal: $selectedDrugOrdinal")
        if (isDrugToLeft()) {
            onSelectedDrugChange(Drug.values()[selectedDrugOrdinal - 1])
        }
    }

    fun isDrugToLeft(): Boolean {
        val selectedDrugOrdinal = selectedDrug.value.ordinal
        if (selectedDrugOrdinal == 0) {
            return false
        }
        return true
    }

    // TODO: Refactor idea - Have a semaphore block until selected drug is changed, then update the flow
    // updates the selected drug.
    private fun onSelectedDrugChange(drug: Drug) {
        _selectedDrug.value = drug
        updateSelectedDrugTakenMg()
    }


    // to be called when a pill is popped, and when a different drug is selected
    private fun updateSelectedDrugTakenMg() {
        val newSelectedPoppedPills =
            allPoppedPills.value.filter { pill -> pill.drug == selectedDrug.value }
        _selectedDrugTakenMg.value = newSelectedPoppedPills.sumOf { it.dosageMg }

        val newSelectedPoppedPillsPreDropOff =
            newSelectedPoppedPills.filter { pill -> !pill.droppedOff }

        _selectedPoppedPillsPreDropOff.value = newSelectedPoppedPillsPreDropOff
        _selectedDrugTakenPreDropOffMg.value =
            newSelectedPoppedPillsPreDropOff.sumOf { it.dosageMg }

        Log.d(
            TAG,
            "updateSelectedDrugTakenMg(): selectedDrugTakenMG: $selectedDrugTakenMg\n\tnewSelectedPoppedPills: $newSelectedPoppedPills, newSelectedDrugTakenPreDropOffMg: "
        )
        _selectedPoppedPills.value = newSelectedPoppedPills


    }

    private val _allPoppedPills = MutableStateFlow<List<Pill>>(emptyList())
    val allPoppedPills: StateFlow<List<Pill>> = _allPoppedPills.asStateFlow()

    private val _selectedPoppedPills = MutableStateFlow<List<Pill>>(emptyList())
    val selectedPoppedPills: StateFlow<List<Pill>> = _selectedPoppedPills.asStateFlow()

    val _selectedDrugTakenMg = MutableStateFlow(0)
    val selectedDrugTakenMg: StateFlow<Int> = _selectedDrugTakenMg.asStateFlow()

    private val _selectedPoppedPillsPreDropOff = MutableStateFlow<List<Pill>>(emptyList())
    val selectedPoppedPillsPreDropOff: StateFlow<List<Pill>> = _selectedPoppedPillsPreDropOff.asStateFlow()

    val _selectedDrugTakenPreDropOffMg = MutableStateFlow(0)
    val selectedDrugTakenPreDropOffMg: StateFlow<Int> = _selectedDrugTakenPreDropOffMg.asStateFlow()


    // returns the total amount of this drug (in mg) consumed
    suspend fun getAmountConsumedMg(drug: Drug): Int {
        return pillRepository.getAmountConsumedMg(drug)
    }


    init {
        /**
         * Only to be updated by changes to the repository!
         * Note: First real-world application of textbook reading was nesting 'onAllPillsChanged' here!
         */
        fun onAllPoppedPillsChange(allNewPills: List<Pill>) {
            Log.d(TAG, "onAllPoppedPillsChange: allNewPills: $allNewPills")
            _allPoppedPills.value = allNewPills
            // filter based on selected drug
//            // TODO: Move this logic elsewhere s.t. popping a pill AND changing selected drug will update this filter
//            val newSelectedPoppedPills =
//                allPoppedPills.value.filter { pill -> pill.drug == selectedDrug.value }
//            _selectedDrugTakenMg.value = newSelectedPoppedPills.sumOf { it.dosageMg }
//            _selectedPoppedPills.value = newSelectedPoppedPills
            updateSelectedDrugTakenMg()
        }

        Log.d(TAG, "init!")
        viewModelScope.launch {
            pillRepository.loadAll().collect { pills ->
                onAllPoppedPillsChange(pills)
            }
        }

    }
}
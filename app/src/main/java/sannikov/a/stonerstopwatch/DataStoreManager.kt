package sannikov.a.stonerstopwatch

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException


class DataStoreManager(val context: Context) {
    private val tag = "DataStoreManager"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "state")

    companion object {
        val STOPWATCH_STATE = intPreferencesKey("STOPWATCH_STATE")
        val PAUSE_TIMESTAMP_MS = longPreferencesKey("PAUSE_TIMESTAMP_MS")
        val START_TIMESTAMP_MS = longPreferencesKey("START_TIMESTAMP_MS")

        fun getKey(keyName: String): Preferences.Key<Any> {
            return when (keyName) {
                "stopwatchState" -> (STOPWATCH_STATE as Preferences.Key<Any>)
                "pauseTimestampMs" -> (PAUSE_TIMESTAMP_MS as Preferences.Key<Any>)
                "startTimestampMs" -> (START_TIMESTAMP_MS as Preferences.Key<Any>)
                else -> {
                    throw InvalidDataStoreKeyException("$keyName is not a valid DataStore key")
                }
            }

        }
    }


    suspend fun save(keyName: String, newValue: Any) {
        Log.d(tag, "savetoDataStore: keyName: $keyName, newValue: " + newValue)
        val key = getKey(keyName)

        context.dataStore.edit { settings ->
            settings[key] = newValue
        }
    }

    suspend fun read(keyName: String): Any? {
        Log.d(tag, "read with keyName: $keyName")
        val key = getKey(keyName = keyName)
        val flow = context.dataStore.data.catch { e ->
            if (e is IOException) {
                Log.d(tag, "read($keyName) encountered an IO exception... emptyingPreferences")
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
            .map { Preferences ->
                Preferences[key] ?: null
            }

        val readValue = flow.first()
        when (key) {
            STOPWATCH_STATE -> {
                return StopwatchStates.values()[readValue as Int]
            }

            START_TIMESTAMP_MS, PAUSE_TIMESTAMP_MS -> {
                if (readValue == null) {
                    return 0L
                }
                return readValue as Long
            }
            else -> {
                throw InvalidDataStoreKeyException("$tag: fell into default case of read function with keyName: $keyName")
            }
        }
    }

    // saves all values
    fun saveState(stateViewModel: StateViewModel) {
        Log.d(tag, "saveState:")

        GlobalScope.launch(Dispatchers.IO) {
            stateViewModel.stopwatchState.getValue()?.let { save("stopwatchState", it.ordinal) }
            stateViewModel.pauseTimestampMs.getValue()?.let { save("pauseTimestampMs", it) }
            stateViewModel.startTimestampMs.getValue()?.let { save("startTimestampMs", it) }
        }
    }
}
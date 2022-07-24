package sannikov.a.stonerstopwatch.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import sannikov.a.stonerstopwatch.InvalidDataStoreKeyException
import java.io.IOException
import javax.inject.Inject


class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {
    private val tag = "DataStoreManager"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "state")

    private val dataStore = appContext.dataStore

    // Makes this a singleton, while also maintaining 'static' values
    /*
    companion object : SingletonHolder<DataStoreManager, Context>(::DataStoreManager) {

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
     */

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

//    companion object : SingletonHolder<DataStoreManager, Context>(::DataStoreManager) {}


    suspend fun save(keyName: String, newValue: Any) {
        Log.d(tag, "save(); keyName: $keyName, newValue: $newValue")
        val key = getKey(keyName)

        dataStore.edit { settings ->
            settings[key] = newValue
        }
    }

    suspend fun read(keyName: String): Any? {
        Log.d(tag, "read with keyName: $keyName")
        val key = getKey(keyName = keyName)
        val flow = dataStore.data.catch { e ->
            if (e is IOException) {
                Log.d(tag, "read($keyName) encountered an IO exception... emptyingPreferences")
                emit(emptyPreferences())
            } else {
                throw e
            }
        }
            .map { Preferences ->
                Preferences[key]
            }

        val readValue = flow.first()
        if(readValue == null) {
            Log.d(tag, "read(): readValue was null!")
        }
        return when (key) {
            STOPWATCH_STATE -> {
                if (readValue != null) StopwatchStates.values()[readValue as Int] else StopwatchStates.PAUSED
            }

            START_TIMESTAMP_MS, PAUSE_TIMESTAMP_MS -> { // TODO: Is 'now' the best default value for both?
                if (readValue != null) readValue as Long else System.currentTimeMillis()
            }
            else -> {
                throw InvalidDataStoreKeyException("$tag: fell into default case of read function with keyName: $keyName")
            }
        }
    }
}
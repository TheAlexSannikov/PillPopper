package sannikov.a.stonerstopwatch

import android.content.Context
import android.util.Log
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


class DataStoreManager(val context: Context) {
    private val tag = "DataStoreManager"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "state")

    companion object {

//        val STOPWATCH_STATE = booleanPreferencesKey("IS_RUNNING")
        val STOPWATCH_STATE = intPreferencesKey("STOPWATCH_STATE")

        fun getKey(keyName: String): Preferences.Key<Any> {
            return when (keyName) {
                "stopwatchState" -> (STOPWATCH_STATE as Preferences.Key<Any>)
                else -> { throw InvalidDataStoreKeyException("$keyName is not a valid DataStore key")}
            }

        }
    }



    suspend fun save(keyName: String, newValue: Any ){
        Log.d(tag, "savetoDataStore: newValue: " + newValue)
        val key = getKey(keyName)
        context.dataStore.edit { settings ->
            settings[key] = newValue
//            isRunning?.let{
//                settings[IS_RUNNING] = isRunning
//            }
        }
    }

    suspend fun read(): Any? {
        val settings = context.dataStore.data.first()
        if(settings == null) {
            return null
        }
        return StopwatchStates.values()[settings[STOPWATCH_STATE]!!]
    }
}
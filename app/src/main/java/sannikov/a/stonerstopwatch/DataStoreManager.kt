package sannikov.a.stonerstopwatch

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class DataStoreManager(val context: Context) {
    private val tag = "DataStoreManager"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "state")

    companion object {

        val IS_RUNNING = booleanPreferencesKey("IS_RUNNING")

        fun getKey(keyName: String): Preferences.Key<Boolean>? {
            when (keyName) {
                "isRunning" -> return IS_RUNNING
            }
            return null
        }
    }
    suspend fun savetoDataStore(isRunning: Boolean?) {
        Log.d(tag, "savetoDataStore: newIsRunning: " + isRunning)
        context.dataStore.edit { settings ->
            isRunning?.let{
                settings[IS_RUNNING] = isRunning
            }
        }
    }

    suspend fun readFromDataStore(): Boolean? {
        val settings = context.dataStore.data.first()
        return settings[IS_RUNNING]
    }
}
package sannikov.a.stonerstopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import sannikov.a.stonerstopwatch.ui.theme.StonerStopwatchTheme


class MainActivity : ComponentActivity() {
    private val tag = "MainActivity"
    lateinit var dataStoreManager: DataStoreManager
    lateinit var stateViewModel : StateViewModel


    @ExperimentalGraphicsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataStoreManager = DataStoreManager(this@MainActivity)
        stateViewModel = StateViewModel(dataStoreManager = dataStoreManager) // to interact with mutableLiveData

        setContent {
            StonerStopwatchTheme() {
                MainScreenBottomNav(stateViewModel =  stateViewModel, dataStoreManager = dataStoreManager)
            }
        }
    }
}




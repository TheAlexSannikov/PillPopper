package sannikov.a.stonerstopwatch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.hsl
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import sannikov.a.stonerstopwatch.ui.theme.StonerStopwatchTheme
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


class MainActivity : ComponentActivity() {
    private val tag = "MainActivity"
    val stateViewModel = StateViewModel() // to interact with mutableLiveData
    lateinit var sharedPreferences: SharedPreferences

    @ExperimentalGraphicsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences= this?.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        setContent {
            StonerStopwatchTheme() {
                MainScreenBottomNav(stateViewModel =  stateViewModel, sharedPreferences = sharedPreferences, context = applicationContext)
            }
        }
    }
}




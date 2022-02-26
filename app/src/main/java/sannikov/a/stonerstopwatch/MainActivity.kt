package sannikov.a.stonerstopwatch

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

const val tag = "MainActivity"
class MainActivity : ComponentActivity() {

    @ExperimentalGraphicsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StonerStopwatchTheme() {
                MainScreenBottomNav()

            }
//            Navigation()
        }

    }
}

/*
setContent {
            Surface(
//                color = Color(0xFF101010),
                color = Color(R.attr.colorPrimary),

                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Timer(
                        totalTime = 100L * 1000L,
                        handleColor = Color.Green,
//                        dayColor = hsl(197F, 0.71F, 0.73F),
//                        nightColor = hsl(236F, 0.61F, 0.20F),
                        dayColor = Color(R.attr.colorPrimary),
                        nightColor = Color(R.attr.colorSecondary),
                        modifier = Modifier.size(200.dp),
                    )
                    Log.d(tag, "colorPrimary"  + R.attr.colorPrimary + ", colorSecondary: " + R.attr.colorSecondary)
                }
            }
        }
 */



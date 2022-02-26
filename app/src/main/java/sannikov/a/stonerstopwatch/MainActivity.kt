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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

const val tag = "MainActivity"
class MainActivity : ComponentActivity() {

    @ExperimentalGraphicsApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation()
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

/*
@Composable
fun Timer(
    totalTime: Long,
    handleColor: Color,
    dayColor: Color,
    nightColor: Color,
    modifier: Modifier = Modifier,
    initArcPercent: Float = 1f,
    strokeWidth: Dp = 5.dp,
) {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    var dayStartArcPercent by remember {
        mutableStateOf(initArcPercent)
    }
    var currentTime by remember {
        mutableStateOf(totalTime)
    }
    var isTimerRunning by remember {
        mutableStateOf(false)
    }
    // run code whenever a key changes
    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if(currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            dayStartArcPercent = currentTime / totalTime.toFloat()
        }
    }

    Box(

        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged {
                size = it
            }
    ) {
        // draw the circular arc of the stopwatch
        Canvas(modifier = modifier) {
            drawArc(
                color = dayColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = nightColor,
                startAngle = -215f,
                sweepAngle = 250f * dayStartArcPercent,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // draw the circle on end of active bar. A little complex
            val center = Offset(size.width / 2f, size.height / 2f)
            val angle =
                (250f * dayStartArcPercent + 145f) * (PI / 180f).toFloat() // degree format of the circle
            val r = size.width / 2f
            val xOff = cos(angle) * r
            val yOff = sin(angle) * r

            drawPoints(
                listOf(Offset(center.x + xOff, center.y + yOff)),
                pointMode = PointMode.Points,
                color = handleColor,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round,
            )
        }
        // the time on clock
        Text(
            text = (currentTime / 1000L).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Button(
            onClick = { isTimerRunning = !isTimerRunning},
            modifier = Modifier.align(Alignment.BottomCenter),
            // can have different colors depending on app state
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (!isTimerRunning || currentTime <= 0L) {
                    Color.Green
                } else {
                    Color.Red
                }
            )
        ){
            Text(text = if(isTimerRunning && currentTime >= 0L) "Stop"
                else if (!isTimerRunning && currentTime >= 0L) "Start"
                else "Restart"
            )
        }
    }

}
*/
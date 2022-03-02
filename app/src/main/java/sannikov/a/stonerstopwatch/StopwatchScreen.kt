package sannikov.a.stonerstopwatch

import android.util.Log
import androidx.compose.foundation.Canvas
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

const val denominatorTime = 100000L
val delayAmountMs = 100L
val tag = "StopwatchScreen"


@Composable
fun StopwatchScreen(
    stateViewModel: StateViewModel,
) {

    val startTimestampMs by stateViewModel.startTimestampMs.collectAsState()
    val stopwatchState by stateViewModel.stopwatchState.collectAsState()
    val pauseTimestampMs by stateViewModel.pauseTimestampMs.collectAsState()
    val displayTime by stateViewModel.displayTime.collectAsState()


    StopwatchContent(
        stateViewModel = stateViewModel,
        stopwatchState = stopwatchState,
        displayTime = displayTime,
        handleColor = Color.Red,
        dayColor = Color(R.attr.colorPrimary),
        nightColor = Color(R.attr.colorSecondary),
        modifier = Modifier.size(200.dp),
    )
}

@Composable
fun StopwatchContent(
    stateViewModel: StateViewModel,
    displayTime: String,
    stopwatchState: StopwatchStates,
    handleColor: Color,
    dayColor: Color,
    nightColor: Color,
    modifier: Modifier = Modifier,
    initArcPercent: Float = 1f,
    strokeWidth: Dp = 5.dp,
) {
    Log.d(tag, "\tdisplayTime: $displayTime")
    val currentTime = stateViewModel.clock.value - stateViewModel.startTimestampMs.value // TODO: Better arc logic
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    var dayStartArcPercent by remember {
        mutableStateOf(initArcPercent)
    }

    // run code whenever a key changes
    LaunchedEffect(key1 = displayTime) {
        if (stopwatchState == StopwatchStates.RUNNING) {
            dayStartArcPercent = currentTime / denominatorTime.toFloat()
        }
    }
    Surface(
        color = Color(R.attr.colorPrimary),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {

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
                // TODO: Make the clock update...
                // the time on clock
                Text(
                    text = displayTime,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Button(
                    onClick = {
                        buttonOnClick(
                            currState = stopwatchState,
                            stateViewModel = stateViewModel,
                        )
                    },
                    modifier = Modifier.align(Alignment.BottomCenter),
                    // can have different colors depending on app state
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Green// if (!isRunning || currentTime <= 0L) {
//                            Color.Green
//                        } else {
//                            Color.Red
//                        }
                    )
                ) {
                    Text(
                        // TODO: include in StopwatchStates sealed class
                        text = if (stopwatchState == StopwatchStates.RUNNING && currentTime >= 0L) "Stop"
                        else "Start"
//                        else if (!isRunning && currentTime >= 0L) "Start"
//                        else "Restart"
                    )
                }
            }
        }
    }
}

fun buttonOnClick(
    currState: StopwatchStates,
    stateViewModel: StateViewModel,
) {
    val newState = if (currState == StopwatchStates.RUNNING) {
        StopwatchStates.PAUSED
    } else {
        StopwatchStates.RUNNING
    }
    stateViewModel.onStopwatchStateChange(newState = newState)

    val currTime = System.currentTimeMillis()

    when (newState) {
        // TODO: handle these casses; save startTime
        StopwatchStates.RUNNING -> {
            val pauseTimestampMs = stateViewModel.pauseTimestampMs.value
            // account for time the timer was paused moving by startTime forward by that amount of time
            val lengthOfPause = currTime - pauseTimestampMs; // TODO: this will probably bug out..
            stateViewModel.onStartTimestampMsChange(newStartTimestampMs = currTime - lengthOfPause)
        }
        StopwatchStates.PAUSED -> {
            stateViewModel.onPauseTimestampMsChange(newPauseTimestampMs = currTime)
        }
    }
}
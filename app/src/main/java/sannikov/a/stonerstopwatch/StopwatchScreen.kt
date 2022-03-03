package sannikov.a.stonerstopwatch

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import sannikov.a.stonerstopwatch.ui.theme.AppTheme
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
        modifier = Modifier.size(200.dp),
    )
}

@Preview
@Composable
fun StopwatchScreenStub() {

    val stateViewModel = StateViewModel()
    val startTimestampMs by stateViewModel.startTimestampMs.collectAsState()
    val stopwatchState by stateViewModel.stopwatchState.collectAsState()
    val pauseTimestampMs by stateViewModel.pauseTimestampMs.collectAsState()
    val displayTime by stateViewModel.displayTime.collectAsState()


    StopwatchContent(
        stateViewModel = stateViewModel,
        stopwatchState = stopwatchState,
        displayTime = displayTime,
        handleColor = Color.Red,
        modifier = Modifier.size(200.dp),
    )
}

@Composable
fun StopwatchContent(
    stateViewModel: StateViewModel,
    displayTime: String,
    stopwatchState: StopwatchStates,
    handleColor: Color,
    modifier: Modifier = Modifier,
    initArcPercent: Float = 1f,
    strokeWidth: Dp = 5.dp,
) {
    val currentTime =
        stateViewModel.clock.value - stateViewModel.startTimestampMs.value // TODO: Better arc logic
    var size by remember { // TODO: Remove?
        mutableStateOf(IntSize.Zero) // IntSize(width = 1000, height = 1000)
    }

    var dayStartArcPercent by remember {
        mutableStateOf(initArcPercent)
    }

    val colorPrimary = AppTheme.colors.primary
    val colorSecondary = AppTheme.colors.secondary

    // run code whenever a key changes
    LaunchedEffect(key1 = displayTime) {
        if (stopwatchState == StopwatchStates.RUNNING) {
            dayStartArcPercent = currentTime / denominatorTime.toFloat()
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {

            Box(

                modifier = modifier
                    .onSizeChanged {
                        size = it
                    }
            ) {
                // draw the circular arc of the stopwatch
                Canvas(modifier = modifier) {
                    drawArc(
                        color = colorPrimary,
                        startAngle = -215f,
                        sweepAngle = 250f,
                        useCenter = false,
                        size = Size(size.width.toFloat(), size.height.toFloat()),
                        style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = colorSecondary,
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
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(vertical = 60.dp)
                        .height(100.dp)
                        .fillMaxWidth()
                ) {
                    // the time on clock
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Surface(color = Color.Transparent) {
                            Text(
                                text = displayTime,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(R.attr.colorOnSecondary),
                            )
                        }

                    }
                    // encapsulate the buttons
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        // start/stop button
                        Button(
                            onClick = {
                                buttonOnClick(
                                    stateViewModel = stateViewModel,
                                    buttonName = "start",
                                )
                            },
                            // can have different colors depending on app state
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (stateViewModel.stopwatchState.value == StopwatchStates.RUNNING) {
                                    Color.Green
                                } else {
                                    Color.Red
                                }
                            )
                        ) {
                            Text(
                                // TODO: include in StopwatchStates sealed class
                                text = if (stopwatchState == StopwatchStates.RUNNING) "Stop"
                                else "Start"
                            )
                        }

                        // reset button
                        Button(
                            onClick = {
                                buttonOnClick(
                                    stateViewModel = stateViewModel,
                                    buttonName = "reset"
                                )
                            },
                            // can have different colors depending on app state
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = if (stateViewModel.stopwatchState.value == StopwatchStates.RUNNING) {
                                    Color.Green
                                } else {
                                    Color.Red
                                }
                            )
                        ) {
                            Text(
                                text = "Reset"
                            )
                        }
                    }
                }

            }
        }
    }
}

fun buttonOnClick(
    stateViewModel: StateViewModel,
    buttonName: String,
) {

    val oldState = stateViewModel.stopwatchState.value
    lateinit var newState: StopwatchStates

    when (buttonName) {
        "start" -> {
            newState = if (oldState == StopwatchStates.RUNNING) {
                StopwatchStates.PAUSED
            } else {
                StopwatchStates.RUNNING
            }
        }

        "reset" -> {
            newState = StopwatchStates.RESET
        }
    }
    Log.d(tag, "$buttonName was clicked, oldState: $oldState, newState: $newState")

    stateViewModel.onStopwatchStateChange(newState = newState)

    val currTime = System.currentTimeMillis()

    when (newState) {
        // TODO: handle these cases; save startTime
        StopwatchStates.RUNNING -> {

            val pauseTimestampMs = stateViewModel.pauseTimestampMs.value
            // account for time the timer was paused moving by startTime forward by that amount of time
            val lengthOfPause = currTime - pauseTimestampMs; // TODO: this will probably bug out..

            Log.d(tag, "\tlengthOfPause was known to be $lengthOfPause")
            stateViewModel.onStartTimestampMsChange(newStartTimestampMs = stateViewModel.startTimestampMs.value + lengthOfPause)
        }
        StopwatchStates.PAUSED -> {
            stateViewModel.onPauseTimestampMsChange(newPauseTimestampMs = currTime)
        }
        StopwatchStates.RESET -> {
            stateViewModel.onStartTimestampMsChange(newStartTimestampMs = currTime)
            stateViewModel.onPauseTimestampMsChange(newPauseTimestampMs = currTime)
            stateViewModel.onClockChange(newClock = currTime, print = true)
        }
    }
}
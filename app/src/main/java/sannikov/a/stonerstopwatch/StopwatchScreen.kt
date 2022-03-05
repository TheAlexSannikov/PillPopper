package sannikov.a.stonerstopwatch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


const val period = 60000L
const val arcStartPosition = 180F // 0 is 3 o'clock, 90 is 12 o'clock
val tag = "StopwatchScreen"
val happyEarthSize = 300.dp

lateinit var happyEarth : Bitmap
@Preview
@Composable
fun StopwatchScreenStub() {
    val stateViewModel = StateViewModel()
    val startTimestampMs by stateViewModel.startTimestampMs.collectAsState()
    val stopwatchState by stateViewModel.stopwatchState.collectAsState()
    val pauseTimestampMs by stateViewModel.pauseTimestampMs.collectAsState()
    val displayTime by stateViewModel.displayTime.collectAsState()
    happyEarth = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.happy_earth_no_bg)


    StopwatchContent(
        stateViewModel = stateViewModel,
        stopwatchState = stopwatchState,
        displayTime = displayTime,
//        handleColor = Color.Red,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun StopwatchScreen(
    stateViewModel: StateViewModel,
) {

    val startTimestampMs by stateViewModel.startTimestampMs.collectAsState()
    val stopwatchState by stateViewModel.stopwatchState.collectAsState()
    val pauseTimestampMs by stateViewModel.pauseTimestampMs.collectAsState()
    val displayTime by stateViewModel.displayTime.collectAsState()
    happyEarth = BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.happy_earth_no_bg)

    StopwatchContent(
        stateViewModel = stateViewModel,
        stopwatchState = stopwatchState,
        displayTime = displayTime,
        modifier = Modifier.aspectRatio(1F),
    )
}


@Composable
fun StopwatchContent(
    stateViewModel: StateViewModel,
    displayTime: String,
    stopwatchState: StopwatchStates,
    modifier: Modifier = Modifier,
) {
//    val currentTime =
//        stateViewModel.clock.value - stateViewModel.startTimestampMs.value // TODO: Better arc logic
//    var size by remember { // TODO: Remove?
//        mutableStateOf(IntSize.Zero) // IntSize(width = 1000, height = 1000)
//    }
//    val width = DisplayMetrics().widthPixels as Float
//    val size = Size(width, width)
    val displayTime by stateViewModel.displayTime.collectAsState()
    // background of app
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background,

        ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
//                .background(Color.Cyan)
                .wrapContentWidth()
                .wrapContentWidth()
                .fillMaxHeight(),
        ) {
            Box(
                modifier = modifier
//                    .onSizeChanged {
//                        size = it
//                    }
                    .requiredSize(happyEarthSize)
                    .align(Alignment.CenterHorizontally)
//                    .background(Color.LightGray)
                    .padding(1.dp)
//                    .wrapContentWidth()
//                    .height(IntrinsicSize.Max)
                    .wrapContentSize()
            ) {
                DrawProgressionArc(stateViewModel = stateViewModel, displayTime = displayTime)
            }

            // the time on clock
            Text(
                style = MaterialTheme.typography.h4,
                text = displayTime,
                color = MaterialTheme.colors.onBackground,
            )

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = modifier
                    .wrapContentHeight()
//                    .background(Color.Red)
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
                        text = if (stopwatchState == StopwatchStates.RUNNING) "Stop" else "Start"
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
                    ),

                    ) {
                    Text(text = "Reset")
                }
            }
        }
    }
}

// Now we're getting somewhere...
@Composable
fun DrawProgressionArc(stateViewModel: StateViewModel, displayTime: String) {
    val colorPrimary = MaterialTheme.colors.primary
    val colorSecondary = MaterialTheme.colors.secondary
    val strokeWidth: Dp = 30.dp
    val currentTime =
        stateViewModel.clock.value - stateViewModel.startTimestampMs.value // TODO: Better arc logic
    val initArcPercent = 1F
    var dayStartArcPercent by remember {
        mutableStateOf(initArcPercent)
    }
    // run code whenever a key changes
    LaunchedEffect(key1 = displayTime) {
//        if (stopwatchState == StopwatchStates.RUNNING) {
        dayStartArcPercent = currentTime / period.toFloat()
//        }
    }
    val painter = painterResource(id = R.drawable.happy_earth_no_bg_paint_2)
    Image(painter = painter, contentDescription = "Happy Earth!", modifier = Modifier.size(
        happyEarthSize))

    // draw the circular arc of the stopwatch
    Canvas(modifier = Modifier
//        .background(Color.Magenta)
        .size(happyEarthSize)) {
        // the under arc
        drawArc(
            color = colorSecondary,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
        )
        // the arc that grows
        drawArc(
            color = colorPrimary,
            startAngle = -arcStartPosition,
            sweepAngle = (360f * dayStartArcPercent)%(360),
            useCenter = false,
            size = Size(size.width, size.height),
            style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
        )

        // draw the circle on end of active bar. A little complex
        val center = Offset(size.width / 2f, size.height / 2f)
        val angle =
            (360 * dayStartArcPercent + arcStartPosition) * (PI / arcStartPosition).toFloat() // degree format of the circle
        val r = size.width / 2f
        val xOff = cos(angle) * r
        val yOff = sin(angle) * r

        drawPoints(
            listOf(Offset(center.x + xOff, center.y + yOff)),
            pointMode = PointMode.Points,
            color = Color.Red,
            strokeWidth = (strokeWidth * 2f).toPx(),
            cap = StrokeCap.Round,
        )
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
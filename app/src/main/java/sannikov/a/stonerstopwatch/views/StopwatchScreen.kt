package sannikov.a.stonerstopwatch.views

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import sannikov.a.stonerstopwatch.R
import sannikov.a.stonerstopwatch.TimeFormat
import sannikov.a.stonerstopwatch.viewmodels.StopwatchViewModel
import sannikov.a.stonerstopwatch.viewmodels.StopwatchStates
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


const val STOPWATCH_PERIOD_MS = 60000L
const val arcStartPosition = 180F // 0 is 3 o'clock, 90 is 12 o'clock
const val TAG_STOPWATCH_SCREEN = "StopwatchScreen"
val happyEarthSize = 300.dp

lateinit var happyEarth: Bitmap

@Preview
@Composable
fun StopwatchScreenStub(stopwatchViewModel : StopwatchViewModel = hiltViewModel()) {
//    @Named("stateViewModel")
//    lateinit var stateViewModel: StateViewModel

    val stopwatchState by stopwatchViewModel.stopwatchState.collectAsState()
    happyEarth =
        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.happy_earth_no_bg)
    val elapsedTimeMs by stopwatchViewModel.elapsedTimeMs.collectAsState()

    StopwatchContent(
        elapsedTimeMs = elapsedTimeMs,
        stopwatchState = stopwatchState,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun StopwatchScreen(stopwatchViewModel : StopwatchViewModel = hiltViewModel()) {

    val stopwatchState by stopwatchViewModel.stopwatchState.collectAsState()
    happyEarth =
        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.happy_earth_no_bg)
    val elapsedTimeMs by stopwatchViewModel.elapsedTimeMs.collectAsState()

    StopwatchContent(
        elapsedTimeMs = elapsedTimeMs,
        stopwatchState = stopwatchState,
        modifier = Modifier.aspectRatio(1F),
    )
}


@Composable
fun StopwatchContent(
    elapsedTimeMs: Long,
    stopwatchState: StopwatchStates,
    modifier: Modifier = Modifier,
) {
    val stateViewModel = hiltViewModel<StopwatchViewModel>()

//    val displayTime by stateViewModel.displayTime.collectAsState()
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
                DrawProgressionArc(elapsedTimeMs = elapsedTimeMs, periodMs = STOPWATCH_PERIOD_MS)
            }

            // the time on clock
            Text(
                style = MaterialTheme.typography.h4,
                text = TimeFormat.format(elapsedTimeMs),
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
                        stopwatchButtonOnClick(
                            buttonName = "start",
                            stopwatchViewModel = stateViewModel,
                        )
                    },
                    // can have different colors depending on app state
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (stateViewModel.stopwatchState.value == StopwatchStates.RUNNING) {
                            MaterialTheme.colors.secondary
                        } else {
                            MaterialTheme.colors.error
                        }
                    )
                ) {
                    Text(
                        // TODO: include in StopwatchStates sealed class
                        text = if (stopwatchState == StopwatchStates.RUNNING) "Stop" else "Start",
                        color = if (stateViewModel.stopwatchState.value == StopwatchStates.RUNNING) {
                            MaterialTheme.colors.onSecondary
                        } else {
                            MaterialTheme.colors.onError
                        }
                    )
                }

                // reset button
                Button(
                    onClick = {
                        stopwatchButtonOnClick(
                            buttonName = "reset",
                            stopwatchViewModel = stateViewModel,
                        )
                    },
                    // can have different colors depending on app state
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error
                    ),

                    ) {
                    Text(text = "Reset", color = MaterialTheme.colors.onError,)
                }
            }
        }
    }
}

// Now we're getting somewhere...
@Composable
fun DrawProgressionArc(elapsedTimeMs: Long, periodMs: Long) {
    val colorPrimary = MaterialTheme.colors.primary
    val colorSecondary = MaterialTheme.colors.secondary
    val colorPrimaryVariant = MaterialTheme.colors.primaryVariant
    val strokeWidth: Dp = 30.dp
    val dayStartArcPercent = elapsedTimeMs / periodMs.toFloat()
    val arcColorToggle = (elapsedTimeMs % (2 * periodMs)) >= periodMs
    val painter = painterResource(id = R.drawable.happy_earth_no_bg_paint_2)
    Image(
        painter = painter, contentDescription = "Happy Earth!", modifier = Modifier.size(
            happyEarthSize
        )
    )

    // draw the circular arc of the stopwatch
    Canvas(
        modifier = Modifier
//        .background(Color.Magenta)
            .size(happyEarthSize)
    ) {
        // the under arc
        drawArc(
            color = if(arcColorToggle) colorSecondary else colorPrimary,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
        )
        // the arc that grows
        drawArc(
            color = if(arcColorToggle) colorPrimary else colorSecondary,
            startAngle = -arcStartPosition,
            sweepAngle = (360f * dayStartArcPercent) % (360),
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

        //draw the 'handle'
        drawPoints(
            listOf(Offset(center.x + xOff, center.y + yOff)),
            pointMode = PointMode.Points,
            color = colorPrimaryVariant,
            strokeWidth = (strokeWidth * 2f).toPx(),
            cap = StrokeCap.Round,
        )
    }

}

fun stopwatchButtonOnClick(
    buttonName: String,
    stopwatchViewModel: StopwatchViewModel, // TODO: Hilt for DI?
) {
    val oldState = stopwatchViewModel.stopwatchState.value
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
    Log.d(TAG_STOPWATCH_SCREEN, "$buttonName was clicked, oldState: $oldState, newState: $newState")

    stopwatchViewModel.onStopwatchStateChange(newState = newState)

    val currTime = System.currentTimeMillis()

    when (newState) {
        // TODO: handle these cases; save startTime
        StopwatchStates.RUNNING -> {

            val pauseTimestampMs = stopwatchViewModel.pauseTimestampMs.value
            // account for time the timer was paused moving by startTime forward by that amount of time
            val lengthOfPause = currTime - pauseTimestampMs; // TODO: this will probably bug out..

            Log.d(TAG_STOPWATCH_SCREEN, "\tlengthOfPause was known to be $lengthOfPause")
            Log.d(TAG_STOPWATCH_SCREEN, "buttonOnClick(); newStartTimestampMs = ${stopwatchViewModel.startTimestampMs.value + lengthOfPause}")
            stopwatchViewModel.onStartTimestampMsChange(newStartTimestampMs = stopwatchViewModel.startTimestampMs.value + lengthOfPause)
        }
        StopwatchStates.PAUSED -> {
            Log.d(TAG_STOPWATCH_SCREEN, "buttonOnClick(); newPauseTimestampMs = $currTime")
            stopwatchViewModel.onPauseTimestampMsChange(newPauseTimestampMs = currTime)
        }
        StopwatchStates.RESET -> {
            Log.d(TAG_STOPWATCH_SCREEN, "buttonOnClick(); newStartTimestampMs = $currTime, newPauseTimestampMs = $currTime, newClock = $currTime")
            stopwatchViewModel.onStartTimestampMsChange(newStartTimestampMs = currTime)
            stopwatchViewModel.onPauseTimestampMsChange(newPauseTimestampMs = currTime)
            stopwatchViewModel.onClockChange(newClock = currTime, print = true)
        }
    }
}
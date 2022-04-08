package sannikov.a.stonerstopwatch.pilltimer

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import sannikov.a.stonerstopwatch.R
import sannikov.a.stonerstopwatch.data.Drug
import sannikov.a.stonerstopwatch.data.Pill
import sannikov.a.stonerstopwatch.viewmodels.PillViewModel
import sannikov.a.stonerstopwatch.views.happyEarth
import sannikov.a.stonerstopwatch.views.happyEarthSize

const val ACETAMINOPHEN_PERIOD = 21600L // 21600000L 6 hours
const val TAG_PILL_TIMER_SCREEN = "PillTimerScreen"

@Composable
fun PillTimerScreen(pillViewModel: PillViewModel = hiltViewModel()) {

    val allPoppedPills by pillViewModel.allPoppedPills.collectAsState()
    val selectedDrug by pillViewModel.selectedDrug.collectAsState()

    happyEarth =
        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.happy_earth_no_bg)

    PillTimerContent(
        pillViewModel = pillViewModel,
        numPillsPopped = allPoppedPills.size,
        selectedPillName = selectedDrug.drugName,
        modifier = Modifier.aspectRatio(1F),
    )
}


@Composable
fun PillTimerContent(
    pillViewModel: PillViewModel,
    numPillsPopped: Int,
    selectedPillName: String,
    modifier: Modifier = Modifier,
) {

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background,

        ) {
        Column(
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.Cyan)
                .wrapContentWidth()
                .wrapContentWidth()
//                .fillMaxHeight(),
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = modifier
                    .wrapContentHeight()
                    .background(Color.Red)
            ) {
                // choose drug to left
                Button(
                    onClick = {
                        pillTimerButtonOnClick(
                            buttonName = "left",
                            pillViewModel = pillViewModel,
                        )
                    },
                    // can have different colors depending on app state
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary

                    )
                ) {
                    Text(
                        text = "left",
                        color = MaterialTheme.colors.onSecondary
                    )
                }

                // pop drug
                Button(
                    onClick = {
                        pillTimerButtonOnClick(
                            buttonName = "pop",
                            pillViewModel = pillViewModel,
                        )
                    },
                    // can have different colors depending on app state
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary

                    )
                ) {
                    Text(
                        text = "Pop $selectedPillName!",
                        color = MaterialTheme.colors.onSecondary
                    )
                }
                // choose drug to right
                Button(
                    onClick = {
                        pillTimerButtonOnClick(
                            buttonName = "right",
                            pillViewModel = pillViewModel,
                        )
                    },
                    // can have different colors depending on app state
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.secondary

                    )
                ) {
                    Text(
                        text = "right",
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = modifier
//                    .wrapContentHeight()
                    .background(Color.Red)
            ) {
                // undo
                Button(
                    onClick = {
                        pillTimerButtonOnClick(
                            buttonName = "undo",
                            pillViewModel = pillViewModel,
                        )
                    },
                    // can have different colors depending on app state
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error

                    )
                ) {
                    Text(
                        text = "undo",
                        color = MaterialTheme.colors.onError
                    )
                }

                // delete all pills button
                Button(
                    onClick = {
                        pillTimerButtonOnClick(
                            buttonName = "purge",
                            pillViewModel = pillViewModel,
                        )
                    },
                    // can have different colors depending on app state
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error

                    )
                ) {
                    Text(
                        text = "PUUURGE!",
                        color = MaterialTheme.colors.onError
                    )
                }
            }
            // text
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = modifier
                    .requiredHeight(30.dp)
//                    .wrapContentHeight()
            ) {
                Text(
                    style = MaterialTheme.typography.body1,
                    text = "$numPillsPopped pills were popped!\n\n${pillViewModel.allPoppedPills.collectAsState()}",
                    color = MaterialTheme.colors.onBackground,
                )
            }
        }
    }
}

fun pillTimerButtonOnClick(
    buttonName: String,
    pillViewModel: PillViewModel, // TODO: Hilt for DI?
) {

    when (buttonName) {
        "pop" -> {
            Log.d(
                TAG_PILL_TIMER_SCREEN,
                "$buttonName was clicked. Popping ${pillViewModel.selectedDrug.value.drugName}!"
            )
            pillViewModel.onPopPill()
        }
        "purge" -> {
            Log.d(TAG_PILL_TIMER_SCREEN, "$buttonName was clicked. Wiping all pills from records!")
            pillViewModel.deleteAllPills()
        }
        "undo" -> {
            Log.d(
                TAG_PILL_TIMER_SCREEN,
                "$buttonName was clicked. undoAction: ${pillViewModel.canUndoPill.value}"
            )
            pillViewModel.onUndoPill()
        }
        "left" -> {
            Log.d(TAG_PILL_TIMER_SCREEN, "$buttonName was clicked. choosing left pill!")
            pillViewModel.onDrugSelectLeft()
        }
        "right" -> {
            Log.d(TAG_PILL_TIMER_SCREEN, "$buttonName was clicked. choosing right pill!")
            pillViewModel.onDrugSelectRight()
        }
    }

}
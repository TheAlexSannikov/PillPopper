package sannikov.a.stonerstopwatch.pilltimer

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import sannikov.a.stonerstopwatch.R
import sannikov.a.stonerstopwatch.data.Pill
import sannikov.a.stonerstopwatch.viewmodels.PillViewModel
import sannikov.a.stonerstopwatch.views.happyEarth

const val ACETAMINOPHEN_PERIOD = 21600L // 21600000L 6 hours
const val TAG_PILL_TIMER_SCREEN = "PillTimerScreen"

@Composable
fun PillTimerScreen(
    pillViewModel: PillViewModel = hiltViewModel(),
    onPillPop: (pillName: String) -> Unit
) {

    val allPoppedPills by pillViewModel.allPoppedPills.collectAsState()
    val selectedDrug by pillViewModel.selectedDrug.collectAsState()

    happyEarth =
        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.happy_earth_no_bg)

    PillTimerContent(
        pillViewModel = pillViewModel,
        allPoppedPills = allPoppedPills,
        selectedPillName = selectedDrug.drugName,
        modifier = Modifier.aspectRatio(1F),
        onPillPop = onPillPop,
    )
}


@Composable
fun PillTimerContent(
    pillViewModel: PillViewModel,
    allPoppedPills: List<Pill>,
    selectedPillName: String,
    modifier: Modifier = Modifier,
    onPillPop: (pillName: String) -> Unit,
) {
    val messages = ArrayList<String>()

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
                            showSnackbarText = onPillPop,
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
                            showSnackbarText = onPillPop,
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
                            showSnackbarText = onPillPop,
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

                // undo
                Button(
                    onClick = {
                        pillTimerButtonOnClick(
                            buttonName = "undo",
                            pillViewModel = pillViewModel,
                            showSnackbarText = onPillPop,
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
                            showSnackbarText = onPillPop,
                        )
                        messages.add("undo?")
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

            Text(
                style = MaterialTheme.typography.body1,
                text = "${allPoppedPills.size} pills were popped!${allPoppedPills}",
                color = MaterialTheme.colors.onBackground,
            )

        }
    }
}

fun pillTimerButtonOnClick(
    buttonName: String,
    pillViewModel: PillViewModel,
    showSnackbarText: (pillName: String) -> Unit, // TODO: Hilt for DI?
) {

    when (buttonName) {
        "pop" -> {
            val pillName = pillViewModel.selectedDrug.value.drugName
            Log.d(
                TAG_PILL_TIMER_SCREEN,
                "$buttonName was clicked. Popping $pillName!"
            )
            pillViewModel.onPopPill()
            showSnackbarText(pillName)
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
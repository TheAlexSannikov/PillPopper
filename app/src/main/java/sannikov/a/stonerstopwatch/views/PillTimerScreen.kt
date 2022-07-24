package sannikov.a.stonerstopwatch.pilltimer

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import sannikov.a.stonerstopwatch.R
import sannikov.a.stonerstopwatch.data.Drug
import sannikov.a.stonerstopwatch.data.Pill
import sannikov.a.stonerstopwatch.data.PillTimerMode
import sannikov.a.stonerstopwatch.viewmodels.PillViewModel
import sannikov.a.stonerstopwatch.views.happyEarth

val PILL_SIZE = 120.dp
val ARROW_SIZE = 90.dp
val ICON_SIZE = 24.dp

@Composable
fun PillTimerScreen(
    pillViewModel: PillViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
) {

    val allPoppedPills by pillViewModel.allPoppedPills.collectAsState()
    val selectedDrug by pillViewModel.selectedDrug.collectAsState()
    val selectedPoppedPills by pillViewModel.selectedPoppedPills.collectAsState()
    val selectedDrugTakenMg by pillViewModel.selectedDrugTakenMg.collectAsState()
    val selectedDrugTakenPreDropOffMg by pillViewModel.selectedDrugTakenPreDropOffMg.collectAsState()
    val selectedPoppedPillsPreDropOff by pillViewModel.selectedPoppedPillsPreDropOff.collectAsState()
    val appMode by pillViewModel.appMode.collectAsState()

    happyEarth =
        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.happy_earth_no_bg)

    PillTimerContent(
        pillViewModel = pillViewModel,
        allPoppedPills = allPoppedPills,
        selectedPoppedPills = selectedPoppedPills,
        selectedDrugTakenMg = selectedDrugTakenMg,
        selectedPoppedPillsPreDropOff = selectedPoppedPillsPreDropOff,
        selectedDrugTakenPreDropOffMg = selectedDrugTakenPreDropOffMg,
        selectedDrug = selectedDrug,
        appMode = appMode,
        modifier = Modifier,
        scaffoldState = scaffoldState,
    )
}

@Composable
fun PillTimerContent(
    pillViewModel: PillViewModel,
    allPoppedPills: List<Pill>,
    selectedPoppedPills: List<Pill>,
    selectedDrugTakenMg: Int,
    selectedPoppedPillsPreDropOff: List<Pill>,
    selectedDrugTakenPreDropOffMg: Int,
    selectedDrug: Drug,
    appMode: PillTimerMode,
    modifier: Modifier.Companion,
    scaffoldState: ScaffoldState
) {
    when (appMode) {
        PillTimerMode.POP_PILLS -> PillTimerPopPillMode(
            pillViewModel,
            allPoppedPills,
            selectedDrug,
            modifier,
            scaffoldState,
            selectedPoppedPills,
            selectedDrugTakenMg,
            selectedPoppedPillsPreDropOff,
            selectedDrugTakenPreDropOffMg
        )
        PillTimerMode.DOSAGE_SELECT -> PillTimerDosageSelectMode(
            pillViewModel,
            selectedDrug,
            modifier,
            scaffoldState
        )
    }
}

@Composable
fun PillTimerDosageSelectMode(
    pillViewModel: PillViewModel,
    selectedDrug: Drug,
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(vertical = 50.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.h6,
                    text = "Choose the dosage for ${selectedDrug.drugName}!",
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            TextButton(
                onClick = {
                    pillViewModel.onDrugSelectLeft()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent
                )
            ) {

                Text(text = "400mg")
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                // choose drug to left
                TextButton(
                    onClick = {
                        pillViewModel.onDrugSelectLeft()
                    },

                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    )
                ) {

                    Text(text = "200mg")
                }

                Surface(
                    modifier = Modifier
                        .size(140.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    if (scaffoldState != null) {
                                        pillViewModel.onPopPill(scaffoldState = scaffoldState)
                                    }
                                },
                                onLongPress = {
                                    pillViewModel.onPillLongPress()
                                }
                            )
                        },
                    shape = CircleShape,
                    color = MaterialTheme.colors.primary,
                ) {
                    Icon(
                        painter = painterResource(id = selectedDrug.imageId),
                        contentDescription = "Pop ${selectedDrug.drugName}",
                        tint = Color.Unspecified
                    )
                }

                // choose drug to right
                TextButton(
                    onClick = {
                        pillViewModel.onDrugSelectRight()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    )
                ) {
                    Text("600mg")
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            ) {
                // choose drug to right
                TextButton(
                    onClick = {
                        pillViewModel.onDrugSelectRight()
                    },
                    // can have different colors depending on app state
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    )
                ) {
                    Text("Custom")
                }

            }
        }
    }
}

@Composable
fun PillTimerPopPillMode(
    pillViewModel: PillViewModel,
    allPoppedPills: List<Pill>,
    selectedDrug: Drug,
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    selectedPoppedPills: List<Pill>,
    selectedDrugTakenMg: Int,
    selectedPoppedPillsPreDropOff: List<Pill>,
    selectedDrugTakenPreDropOffMg: Int,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(vertical = 50.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.h6,
                    text = "${selectedDrugTakenPreDropOffMg}mg of ${selectedDrug.drugName} popped!",
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(vertical = 50.dp)
            ) {
                Text(
                    style = MaterialTheme.typography.body1,
                    text = "And ${selectedDrugTakenMg}mg within 24hr",
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                // choose drug to left
                TextButton(
                    onClick = {
                        pillViewModel.onDrugSelectLeft()
                    },
                    // can have different colors depending on app state
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    )
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_left_90),
                        contentDescription = "Previous pill",
                        tint = if (pillViewModel.isDrugToLeft()) MaterialTheme.colors.onBackground else Color.Gray
                    )
                }

                Surface(
                    modifier = Modifier
                        .size(140.dp)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    if (scaffoldState != null) {
                                        pillViewModel.onPopPill(scaffoldState = scaffoldState)
                                    }
                                },
                                onLongPress = {
                                    pillViewModel.onPillLongPress()
                                },
                            )
                        },
                    shape = CircleShape,
                    color = MaterialTheme.colors.primary,
                ) {
                    Icon(
                        painter = painterResource(id = selectedDrug.imageId),
                        contentDescription = "Pop ${selectedDrug.drugName}",
                        tint = Color.Unspecified
                    )
                }

                // choose drug to right
                TextButton(
                    onClick = {
                        pillViewModel.onDrugSelectRight()
                    },
                    // can have different colors depending on app state
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_right_90),
                        contentDescription = "Next pill",
                        tint = if (pillViewModel.isDrugToRight()) MaterialTheme.colors.onBackground else Color.Gray
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(top = 50.dp)
            ) {
                // undo
                Button(
                    onClick = {
                        pillViewModel.onUndoPill()
                    },
                    // can have different colors depending on app state
                    modifier = Modifier.padding(horizontal = 15.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_undo_24),
                        contentDescription = "Undo",
                        tint = Color.Unspecified
                    )
                }

                // delete all pills button
                Button(
                    onClick = {
                        pillViewModel.deleteAllPills()
                    },
                    // can have different colors depending on app state
                    modifier = Modifier.padding(horizontal = 15.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error

                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                        contentDescription = "Delete all pills",
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}
package sannikov.a.stonerstopwatch.pilltimer

import android.graphics.BitmapFactory
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.ui.text.input.KeyboardType

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
    modifier: Modifier,
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

// TODO: merge this with PillTimerPopPills(), include animations
@Composable
fun PillTimerDosageSelectMode(
    pillViewModel: PillViewModel,
    selectedDrug: Drug,
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
) {
    Surface(
        modifier = modifier
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
                    .weight(3f, fill = true)
                    .fillMaxWidth()
            ) {
                Text(
                    style = MaterialTheme.typography.h6,
                    text = "Select dosage",
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = modifier
                    .weight(1f, fill = true)
                    .fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        pillViewModel.onDrugSelectLeft()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    modifier = modifier
                        .weight(0.2f)
                        .align(Alignment.Top),
                    contentPadding = PaddingValues(0.dp),
                    elevation = null,
                ) {
                    Text("400mg")
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .weight(3f, fill = true)
                    .fillMaxWidth()
            ) {

                Button(
                    onClick = {
                        pillViewModel.onDrugSelectLeft()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    modifier = modifier.weight(0.2f),
                    contentPadding = PaddingValues(0.dp),
                    elevation = null,
                ) {
                    Text("200mg")
                }

                // pop button
                Surface(
                    modifier = modifier
                        .weight(0.4f)
                        .aspectRatio(1f)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    pillViewModel.onPopPill(scaffoldState = scaffoldState)
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
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                    )
                }

                Button(
                    onClick = {
                        pillViewModel.onDrugSelectRight()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    modifier = modifier
                        .weight(0.2f)
                        .padding(0.dp),
                    contentPadding = PaddingValues(0.dp),
                    elevation = null,
                ) {
                    Text("600mg")
                }
            }

            // enter custom dosage
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .weight(1f, fill = true)
                    .fillMaxWidth()
            ) {

                Button(
                    onClick = { pillViewModel.onPromptCustomDosage() },
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .padding(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error
                    ),
                    elevation = null,
                ) {
                    promptDosageInput(scaffoldState = scaffoldState)
                    Text("Custom")
                }
            }
            Spacer(
                modifier = modifier
                    .weight(1.5f, fill = true)
                    .fillMaxWidth()
            )
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
        modifier = modifier
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
                    .weight(3f, fill = true)
                    .fillMaxWidth()
            ) {
                Text(
                    style = MaterialTheme.typography.h6,
                    text = "${selectedDrugTakenPreDropOffMg}mg of ${selectedDrug.drugName} popped!",
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = modifier
                    .weight(1f, fill = true)
                    .fillMaxWidth()
            ) {
                Text(
                    style = MaterialTheme.typography.body1,
                    text = "And ${selectedDrugTakenMg}mg within 24hr",
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Top)
                        .padding(5.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .weight(3f, fill = true)
                    .fillMaxWidth()
            ) {
                // choose drug to left
                Button(
                    onClick = {
                        pillViewModel.onDrugSelectLeft()
                    },
                    // can have different colors depending on app state
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    modifier = modifier.weight(0.2f),
                    contentPadding = PaddingValues(0.dp),
                    elevation = null,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_left_24),
                        contentDescription = "Previous pill",
                        tint = if (pillViewModel.isDrugToLeft()) MaterialTheme.colors.onBackground else Color.Gray,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // pop button
                Surface(
                    modifier = modifier
                        .weight(0.4f)
                        .aspectRatio(1f)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    pillViewModel.onPopPill(scaffoldState = scaffoldState)
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
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp)
                    )
                }

                // choose drug to right
                Button(
                    onClick = {
                        pillViewModel.onDrugSelectRight()
                    },
                    // can have different colors depending on app state
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    modifier = modifier
                        .weight(0.2f)
                        .padding(0.dp),
                    contentPadding = PaddingValues(0.dp),
                    elevation = null,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_arrow_right_24),
                        contentDescription = "Next pill",
                        tint = if (pillViewModel.isDrugToRight()) MaterialTheme.colors.onBackground else Color.Gray,
                        modifier = modifier
                            .fillMaxSize()
                            .padding(0.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .weight(1f, fill = true)
                    .fillMaxWidth()
            ) {
                // undo
                Button(
                    onClick = {
                        pillViewModel.onUndoPill()
                    },
                    // can have different colors depending on app state
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .padding(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error
                    ),
                    elevation = null,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_undo_24),
                        contentDescription = "Undo",
                        tint = Color.Unspecified,
                    )
                }

                // delete all pills button
                Button(
                    onClick = {
                        pillViewModel.deleteAllPills()
                    },
                    // can have different colors depending on app state
                    modifier = Modifier.padding(horizontal = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.error

                    ),
                    elevation = null,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_delete_24),
                        contentDescription = "Delete all pills",
                        tint = Color.Unspecified
                    )
                }
            }
            Spacer(
                modifier = modifier
                    .weight(1.5f, fill = true)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun promptDosageInput(
    pillViewModel: PillViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
) {
    val openDialog by pillViewModel.promptCustomDosage.collectAsState()

    var input by remember { mutableStateOf("0") }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                pillViewModel.onDismissCustomDosage()
            },
            title = {
                Text(text = "Enter dosage")
            },
            text = {
                Column() {
                    TextField(
                        value = input,
                        placeholder = { Text("Custom") },
                        onValueChange = { input = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text("this will be saved for future reference")
                }
            },
            buttons = {
                Row(
                    modifier = Modifier
                        .padding(all = 8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(end = 10.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.primaryVariant
                        ),
                        onClick = { pillViewModel.onDismissCustomDosage() }
                    ) {
                        Text("Cancel")
                    }
                    Button(
                        modifier = Modifier.wrapContentWidth(),
                        onClick = {
                            pillViewModel.onEnterCustomDosage(
                                input.toIntOrNull(),
                                scaffoldState = scaffoldState
                            )
                        }
                    ) {
                        Text("Set dosage")
                    }
                }
            }
        )
    }
}


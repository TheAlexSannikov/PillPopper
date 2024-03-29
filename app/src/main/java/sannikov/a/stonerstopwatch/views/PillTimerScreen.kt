package sannikov.a.stonerstopwatch.pilltimer

import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import sannikov.a.stonerstopwatch.R
import sannikov.a.stonerstopwatch.data.Drug
import sannikov.a.stonerstopwatch.data.Pill
import sannikov.a.stonerstopwatch.viewmodels.PillViewModel
import sannikov.a.stonerstopwatch.views.happyEarth
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text

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
    val isPoppingEnabled by pillViewModel.isPoppingEnabled.collectAsState()
    val showDrugMaximumDialog by pillViewModel.showDrugMaximumDialog.collectAsState()

    happyEarth =
        BitmapFactory.decodeResource(LocalContext.current.resources, R.drawable.happy_earth_no_bg)

    PillTimerPopPillMode(
        pillViewModel,
        allPoppedPills,
        selectedDrug,
        isPoppingEnabled,
        showDrugMaximumDialog,
        Modifier,
        scaffoldState,
        selectedPoppedPills,
        selectedDrugTakenMg,
        selectedPoppedPillsPreDropOff,
        selectedDrugTakenPreDropOffMg
    )
}

@Composable
fun PillTimerPopPillMode(
    pillViewModel: PillViewModel,
    allPoppedPills: List<Pill>,
    selectedDrug: Drug,
    isPoppingEnabled: Boolean,
    showDrugMaximumDialog: Boolean,
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
                Button(
                    onClick = {
                        pillViewModel.onPopPill(scaffoldState = scaffoldState)
                    },
                    modifier = modifier
                        .weight(0.4f)
                        .aspectRatio(1f),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isPoppingEnabled) MaterialTheme.colors.primary else Color.LightGray,
                    ),
                ) {
                    promptDrugMaximumDialog(showDrugMaximumDialog, scaffoldState = scaffoldState)
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
                        backgroundColor = MaterialTheme.colors.error,
                    ),
                    elevation = null,
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_undo_24),
                        contentDescription = "Undo",
                        tint = Color.White,
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
                        tint = Color.White,
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
fun promptDrugMaximumDialog(
    openDialog: Boolean,
    pillViewModel: PillViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState
) {
//    var input by remember { mutableStateOf("0") }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                pillViewModel.onDismissCustomDosage()
            },
            title = {
                Text(text = "Whoa mamma! You've reached your limit for the minute")
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
                        Text("Dismiss")
                    }
                    Button(
                        modifier = Modifier.wrapContentWidth(),
                        onClick = {
                            pillViewModel.onPopPill(scaffoldState, overrideDrugMaximum = true)
                            pillViewModel.onDismissCustomDosage()
                        }
                    ) {
                        Text("Take anyway!")
                    }
                }
            }
        )
    }
}


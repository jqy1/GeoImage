package nz.ac.canterbury.seng440.geoimage.utils

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import nz.ac.canterbury.seng440.geoimage.LocalTripViewModel
import nz.ac.canterbury.seng440.geoimage.R
import nz.ac.canterbury.seng440.geoimage.model.Trip
import nz.ac.canterbury.seng440.geoimage.ui.theme.ButtonWithIcon_EndPadding
import nz.ac.canterbury.seng440.geoimage.ui.theme.Card_Shape

@Composable
fun TripPanelAdd(navController: NavController,tripUIAction: (TripEvent<Trip>) -> Unit){
    val viewModel = LocalTripViewModel.current
    Column {
        var tripName by remember { mutableStateOf("") }
        var tag by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }

        var context = LocalContext.current
        var modifier = Modifier.fillMaxWidth().padding(Card_Shape)


        OutlinedTextField(
            value = tripName,
            modifier = modifier,
            onValueChange = { tripName = it },
            label = { Text(stringResource(id = R.string.app_dialog_trip_name)) }
        )

        OutlinedTextField(
            value = tag,
            modifier = modifier,
            onValueChange = { tag = it },
            label = { Text(stringResource(id = R.string.app_dialog_trip_tag)) }
        )

        OutlinedTextField(
            value = description,
            modifier = modifier,
            onValueChange = { description = it },
            label = { Text(stringResource(id = R.string.app_dialog_trip_description)) }
        )
        Row(modifier = Modifier
            .padding(ButtonWithIcon_EndPadding)
            .align(Alignment.End)){
            TextButton(onClick = {
                var trip = Trip()
                var event : TripEvent<Trip>  = TripEvent<Trip>(ScreenEvent.ScreenTripEditClicked,trip)
                tripUIAction( event )
            }) {
                Text( stringResource(R.string.app_cancel) )
            }
            TextButton(onClick = {

                var trip = Trip(tripName,tag,description)
                var event : TripEvent<Trip>  = TripEvent<Trip>(ScreenEvent.ScreenTripAddClicked,trip)
                tripUIAction( event )

            }) {
                Text( stringResource(R.string.app_save) )
            }
        }
    }
}

@Composable
fun AddTrip(navController: NavController) {
    Column(modifier = Modifier.wrapContentHeight()) {
        var context = LocalContext.current
        var viewModel = LocalTripViewModel.current
        var enableDialog = viewModel.enableDialog.collectAsState()

        if (enableDialog.value) {
            AlertDialog(
                modifier = Modifier.padding(Card_Shape),
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    viewModel.onEnableDialog(false)
                },
                title ={  },
                text = {
                    TripPanelAdd(navController){ tripUIAction ->
                        when(tripUIAction.screenState){
                            is ScreenEvent.ScreenTripAddClicked ->{
                                viewModel.onEnableDialog(false)
                                // save to databases
                                viewModel.addTrip(tripUIAction.t)
                            }
                        }
                    }
                },
                confirmButton = {},
                dismissButton = {}
            )
        }
    }
}



package nz.ac.canterbury.seng440.geoimage.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import nz.ac.canterbury.seng440.geoimage.model.Trip
import nz.ac.canterbury.seng440.geoimage.service.share.ShareSystem
import nz.ac.canterbury.seng440.geoimage.view.GeoImageViewModel
import nz.ac.canterbury.seng440.geoimage.ui.theme.*
import nz.ac.canterbury.seng440.geoimage.utils.ScreenEvent
import nz.ac.canterbury.seng440.geoimage.utils.TripEvent

enum class CustomDialogPosition {
    BOTTOM, TOP
}

fun Modifier.customDialogModifier(pos: CustomDialogPosition) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints);
    layout(constraints.maxWidth, constraints.maxHeight){
        when(pos) {
            CustomDialogPosition.BOTTOM -> {
                placeable.place(0, constraints.maxHeight - placeable.height, 1f)
            }
            CustomDialogPosition.TOP -> {
                placeable.place(0,0,1f)
            }
        }
    }
}


@Composable
fun DialogAction(viewModel: GeoImageViewModel,  onClick : (TripEvent<Trip>) -> Unit) {
    Column(modifier = Modifier.wrapContentHeight()) {
        var context = LocalContext.current
        var openAlertDialog = viewModel.showMyTripDialog.collectAsState()
        val openDialog = remember { mutableStateOf(false)  }

        if (openAlertDialog.value) {
            AlertDialog(
                modifier = Modifier.background(MaterialTheme.colors.onSurface),
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    viewModel.onOpenMyTipClicked(false)

                },
                title ={  },
                text = {
                    Box(modifier = Modifier.padding(Card_Horizontal_Inner_Space)){
                        Box(modifier = Modifier.align(Alignment.BottomCenter)){
                            Column(modifier = Modifier
                                .padding(Card_Horizontal_Inner_Space)
                                .wrapContentHeight().fillMaxWidth().clip(shape = MaterialTheme.shapes.medium)) {
                                    //Card(modifier = Modifier.height(Card_SizeOfActionIcon).background(MaterialTheme.colors.primary).fillMaxWidth()){
                                    //    TextButton(onClick = { makeToast(context,"invoke Edit Name") }) {
                                    //        Text(text = "Edit Name")
                                    //    }
                                    //}
                                    Spacer(modifier = Modifier.height(Card_Anchord_Margin))
                                    Card(modifier = Modifier.padding(start = 4.dp,top=4.dp).height(Card_SizeOfActionIcon)
                                        .fillMaxWidth(1f).background(MaterialTheme.colors.surface)){
                                        TextButton(onClick = {
                                            //shareWeb(context)
                                            var shareSystme = ShareSystem("app",context)
                                            var shareParameters = mapOf("type" to "text/plain", "text" to "https://www.youtube.com/watch?v=JZehdUU6VbQ")
                                            shareSystme.execute( "shareText", shareParameters)
                                        }) {
                                            Text(text = "Share trip", style = MaterialTheme.typography.h6)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(Card_Anchord_Margin))
                                    Card(modifier = Modifier.padding(start = 4.dp,top=4.dp).height(Card_SizeOfActionIcon)
                                        .fillMaxWidth(1f).background(MaterialTheme.colors.surface)){
                                        TextButton(onClick = {
                                            var trip = Trip()
                                            var event : TripEvent<Trip>  = TripEvent<Trip>(
                                                ScreenEvent.ScreeTripShareClicked,trip)
                                            onClick(event)
                                        }) {
                                            Text(text = "Nearby trip", style = MaterialTheme.typography.h6)
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(Card_Anchord_Margin))
                                    Card(modifier = Modifier.padding(start = 4.dp,top=4.dp).height(Card_SizeOfActionIcon)
                                        .fillMaxWidth(1f).background(MaterialTheme.colors.surface)){
                                        TextButton(onClick = {
                                            var trip = Trip()
                                            var event : TripEvent<Trip>  = TripEvent<Trip>(
                                                ScreenEvent.ScreenTripDeleteClicked,trip)
                                            onClick(event)
                                        }) {
                                            Text(text = "Delete Trip", style = MaterialTheme.typography.h6)
                                        }
                                    }
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

package nz.ac.canterbury.seng440.geoimage.ui.screen

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import nz.ac.canterbury.seng440.geoimage.GeoImageApp
import nz.ac.canterbury.seng440.geoimage.LocalPhotoViewModel
import nz.ac.canterbury.seng440.geoimage.LocalTripViewModel
import nz.ac.canterbury.seng440.geoimage.R
import nz.ac.canterbury.seng440.geoimage.model.File
import nz.ac.canterbury.seng440.geoimage.model.Trip
import nz.ac.canterbury.seng440.geoimage.ui.navigation.BottomNavItem
import nz.ac.canterbury.seng440.geoimage.ui.theme.*
import nz.ac.canterbury.seng440.geoimage.utils.AddTrip
import java.net.URLEncoder

@Composable
fun RecorderHeader(trip: Trip, onClick:(trip:Trip)->Unit) {
    val openDialog = remember { mutableStateOf(false)  }
    val tripName = remember { mutableStateOf(trip.name) }
    Row(
        modifier = Modifier
            .height(160.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
        ) {
            Box(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = trip.name,
                    color = Black,
                    fontSize = FABText_SizeOfHeader,
                    fontWeight = FontWeight.Bold

                )
            }
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                IconButton(onClick = { openDialog.value = true  }, modifier = Modifier.padding(Button_Padding)) {
                    Icon(
                        modifier = Modifier.size(Card_Icon_SmallSize),
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "Edit Icon",
                        tint = MaterialTheme.colors.onSurface
                    )
                    if (openDialog.value) {
                        AlertDialog(
                            onDismissRequest = {
                                // Dismiss the dialog when the user clicks outside the dialog or on the back
                                // button. If you want to disable that functionality, simply use an empty
                                // onCloseRequest.
                                openDialog.value = false
                            },
                            title = {
                                Text(text = stringResource(R.string.edit_tripName))
                            },
                            text = {
                                TextField(value = tripName.value, onValueChange = {
                                    tripName.value = it
                                    trip.name = it
                                } )
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                       onClick(trip)
                                        openDialog.value = false
                                    }) {
                                    Text("Confirm")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = {
                                        openDialog.value = false
                                    }) {
                                    Text("Dismiss")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PhotoListItem(photo: File, navController: NavController) {
    val context = LocalContext.current

    Card(modifier = Modifier.height(Photo_Card_Height)) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable {
                    navController.navigate(
                        BottomNavItem.PhotoDetail.screen_route + "/" + URLEncoder.encode(
                            photo.path,
                            "UTF-8"
                        )
                    )
                }
        ) {
            val painter = rememberImagePainter(data = java.io.File(photo.path))
            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
        }
    }

}


@Composable
fun PhotoList(navController: NavController, trip: Trip) {
    //val photos = remember { DataProvider.photoList }
    val photos = LocalPhotoViewModel.current.photo.collectAsState()
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        photos.value.let {
            items(it.size) { it ->
                if(it>0){
                    for ((key, v) in photos.value) {
                        for (file in v) {
                            PhotoListItem(file, navController)
                        }
                    }
                }
            }
        } ?:run{
            Log.d("UI/HomeScreenFlow/${Thread.currentThread().name}","LazyList load data----")
            item( ) {
                CircularProgressIndicator()
            }
        }
    }
}


@Composable
fun BottomButtons(navController: NavController) {
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsMap ->
            val allPermissionsGranted = permissionsMap.values.reduce { acc, next -> acc && next }
            if (allPermissionsGranted) {
                Toast.makeText(context, "All permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "Permission not granted: ${permissionsMap.filter { !it.value }.map { it.key }}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(FABMenuBar_SpaceFromBoundary),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(modifier = Modifier
            .weight(1f, fill = false)
            .width(Button_MaxWidth)
            .height(Button_Height),
            onClick = {
                requestPermissions(context, launcher)
                navController.navigate(BottomNavItem.Trip.screen_route)
            })
        {
            Text(text = "Start")
        }
        Button(modifier = Modifier
            .weight(1f, fill = false)
            .width(Button_MaxWidth)
            .height(Button_Height),
            onClick = { navController.navigate(BottomNavItem.MyTrip.screen_route) }) {
            Text(text = "Trip Details")
        }

    }
}

@Composable
fun RecordScreen(navController: NavController, trip: Long) {
    var trip = GeoImageApp.instance.database.tripDao().findByName(trip)
    var context = LocalTripViewModel.current

    Column(modifier = Modifier.fillMaxWidth()) {
        RecorderHeader(trip){
            context.updateTrip(it)
        }
        Box(
            modifier = Modifier
                .weight(1f, fill = false)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            PhotoList(navController, trip)
        }
        BottomButtons(navController)
    }
}

@Composable
fun FlashScreen(navController: NavController) {
    val viewModel = LocalTripViewModel.current

    AddTrip(navController)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.size(Welcome_Size, Welcome_Size)) {
            OutlinedButton(modifier = Modifier
                //.fillMaxSize()
                .padding(Card_Shape)
                .fillMaxWidth()
                .height(Photo_Card_Height),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onBackground),
                onClick = {}) {
                Text(
                    modifier = Modifier.background(MaterialTheme.colors.surface),
                    text = stringResource(id = R.string.app_welcome),
                    textAlign = TextAlign.Center,
                    fontSize = Welcome_font_size
                )
            }

        }
        Button(modifier = Modifier
            .weight(1f, fill = false)
            .width(Button_MaxWidth)
            .height(Button_Height),
            onClick = {
                viewModel.onEnableDialog(true)
            }) {
            Text(text = stringResource(id = R.string.app_dialog_add_trip))
        }
    }
}

fun requestPermissions(
    context: Context, launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
) {

    val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    if (!permissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }) {
        launcher.launch(permissions)
    }
}


@Preview(showBackground = true)
@Composable
fun RecordPreview() {
    RecordScreen(navController = rememberNavController(), 1)
}
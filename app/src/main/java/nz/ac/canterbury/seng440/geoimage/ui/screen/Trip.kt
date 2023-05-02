package nz.ac.canterbury.seng440.geoimage.ui.screen


import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay
import nz.ac.canterbury.seng440.geoimage.data.GeoImageDatabase
import nz.ac.canterbury.seng440.geoimage.model.LocationPoint
import nz.ac.canterbury.seng440.geoimage.ui.component.makeToast
import nz.ac.canterbury.seng440.geoimage.ui.navigation.BottomNavItem
import nz.ac.canterbury.seng440.geoimage.ui.theme.FABMenuBar_SizeOfBigActionIcon
import nz.ac.canterbury.seng440.geoimage.ui.theme.FABMenuBar_SpaceFromBoundary
import nz.ac.canterbury.seng440.geoimage.utils.LocationLiveData
import nz.ac.canterbury.seng440.geoimage.utils.LocationViewModel

@Composable
fun TripScreen(navController: NavController, tripId: Long) {

//    Column {
//
//        Box(modifier = Modifier.weight(1f, fill = false)) {
//            GoogleMapScreen()
//        }
//        AppBar(navController)
//    }

    val context = LocalContext.current
    val locations = GeoImageDatabase.getDatabase(context).locationPointDao().getAll()
    val myLocation by LocationViewModel(context).getLocationLiveData().observeAsState()


    Column(
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(modifier = Modifier.weight(1f, fill = false)) {
            GoogleMapScreen(locations, myLocation)
        }
        AppBar(navController)
    }
    LaunchedEffect(Unit) {
        while (true) {
            myLocation?.let {
                val locationPoint = LocationPoint(it, tripId)
                Toast.makeText(context, "Location Updated", Toast.LENGTH_SHORT).show()
                GeoImageDatabase.getDatabase(context).locationPointDao().insert(locationPoint)
            }
            delay(LocationLiveData.UPDATE_INTERVAL)
        }
    }
}

@Composable
fun GoogleMapScreen(locationPoints: List<LocationPoint>, myLocation: Location?) {
    val properties by remember {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL))
    }
    val uiSettings by remember { mutableStateOf(MapUiSettings()) }
    val cameraPositionState = rememberCameraPositionState()
    myLocation?.let {
        cameraPositionState.position =
            CameraPosition.fromLatLngZoom(LatLng(it.latitude, it.longitude), 15f)
    }

    GoogleMap(
        modifier = Modifier,
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) {
        for (location in locationPoints) {
            Marker(state = MarkerState(position = LatLng(location.latitude, location.longitude)))
        }


//        Marker(
//            state = MarkerState(position = singapore),
//            title = "Singapore",
//            snippet = "Marker in Singapore",
//        )
    }

}


@Composable
fun AppBar(navController: NavController) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(FABMenuBar_SpaceFromBoundary),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Button(
            modifier = Modifier.weight(1f, fill = false),
            onClick = { (navController.popBackStack()) },
            colors = ButtonDefaults.buttonColors(MaterialTheme.colors.error)
        ) {
            Text(text = "Stop")
        }
        IconButton(modifier = Modifier
            .size(
                FABMenuBar_SizeOfBigActionIcon, FABMenuBar_SizeOfBigActionIcon
            )
            .weight(1f), onClick = {
            navController.navigate(BottomNavItem.TripCamera.screen_route)
        }) {
            Icon(
                modifier = Modifier.size(FABMenuBar_SizeOfBigActionIcon),
                imageVector = Icons.Outlined.PhotoCamera,
                contentDescription = "Camera",
                tint = MaterialTheme.colors.onSurface
            )
        }
        Button(modifier = Modifier.weight(1f, fill = false), onClick = { makeToast(context) }) {
            Text(text = "Detail")
        }
    }
}


@Preview
@Composable
fun TripScreenPreview() {
    TripScreen(navController = rememberNavController(), 0)
}


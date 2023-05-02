package nz.ac.canterbury.seng440.geoimage.ui.screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.exifinterface.media.ExifInterface
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import nz.ac.canterbury.seng440.geoimage.GeoImageApp
import nz.ac.canterbury.seng440.geoimage.R
import nz.ac.canterbury.seng440.geoimage.data.GeoImageDatabase
import nz.ac.canterbury.seng440.geoimage.model.LocationPoint
import nz.ac.canterbury.seng440.geoimage.model.Route
import nz.ac.canterbury.seng440.geoimage.ui.component.makeToast
import nz.ac.canterbury.seng440.geoimage.utils.CameraView
import nz.ac.canterbury.seng440.geoimage.utils.getRealPathFromURI
import java.io.File

@Composable
fun TripCameraScreen(navController: NavController, tripId: Long) {
    val context = LocalContext.current

    CameraView(navController = navController, onImageCaptured = { uri, isLocationSavedToFile ->
        run {
            Log.d("Trip Screen", "Image Uri Captured from Camera View $uri")
            val file = File(getRealPathFromURI(context, uri)!!)
            ExifInterface(file).latLong.let {
                val locationPoint = LocationPoint(it!![0], it[1])
                uri.path?.let { it1 ->
                    run {
                        val locationPointId =
                            GeoImageDatabase.getDatabase(context).locationPointDao()
                                .insert(locationPoint)
                        val route = Route(tripId = tripId, "", "")
                        val routeId = GeoImageDatabase.getDatabase(context).routeDao().insert(route)
                        val photo = nz.ac.canterbury.seng440.geoimage.model.File(
                            routeId, locationPointId, "", "", file.path,
                            it1
                        )
                        GeoImageDatabase.getDatabase(context).fileDao().insert(photo)
                    }
                } ?: run {
                    val txtMessage = GeoImageApp.instance.getString(R.string.app_dialog_error_info)
                    txtMessage.replace("#Error#", file.path)
                    makeToast(context, "")
                }
            }


        }
    }, onError = { error ->
        Log.d("Trip Screen", "$error")
    })
}

@Preview
@Composable
fun TripCameraScreenPreview() {
    TripCameraScreen(navController = rememberNavController(), tripId = 0)
}
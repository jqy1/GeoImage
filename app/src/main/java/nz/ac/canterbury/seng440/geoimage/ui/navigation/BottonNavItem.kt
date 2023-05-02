package nz.ac.canterbury.seng440.geoimage.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import nz.ac.canterbury.seng440.geoimage.GeoImageApp
import nz.ac.canterbury.seng440.geoimage.R

sealed class BottomNavItem(var title:String, var imageVector : ImageVector, var screen_route:String){
    object Home : BottomNavItem( GeoImageApp.instance.getString(R.string.app_navi_record), Icons.Default.Home,"Record")
    object MyTrip: BottomNavItem(GeoImageApp.instance.getString(R.string.app_navi_my_trip),Icons.Default.Place,"My Trip")
    object Setting: BottomNavItem(GeoImageApp.instance.getString(R.string.app_navi_setting),Icons.Default.Settings,"Setting")
    object Trip: BottomNavItem(GeoImageApp.instance.getString(R.string.app_navi_my_trip),Icons.Default.Camera,"trip_screen")
    object TripCamera: BottomNavItem(GeoImageApp.instance.getString(R.string.app_navi_my_trip),Icons.Default.Camera,"trip_camera_screen")
    object PhotoDetail: BottomNavItem("Photo Detail", Icons.Default.Camera, "photo_detail_screen")


}
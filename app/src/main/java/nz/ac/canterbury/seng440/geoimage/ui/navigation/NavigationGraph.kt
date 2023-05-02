package nz.ac.canterbury.seng440.geoimage.ui.navigation

import android.util.Log
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import nz.ac.canterbury.seng440.geoimage.LocalPhotoViewModel
import nz.ac.canterbury.seng440.geoimage.LocalTripViewModel
import nz.ac.canterbury.seng440.geoimage.ui.screen.*
import nz.ac.canterbury.seng440.geoimage.view.GeoImageViewModel

@Composable
fun SetupNavGraph(navController: NavHostController,viewModel: GeoImageViewModel) {

    NavHost(navController = navController, startDestination = BottomNavItem.Home.screen_route) {
         composable(route = BottomNavItem.Trip.screen_route,) {
             LocalTripViewModel.current.onLoadingLastTripId()
             var lastTripId = LocalTripViewModel.current.lastTripId.collectAsState()
             if (lastTripId.value > 0){
                 //Log.d("NavGraph/TripScreen","{$lastTripId}")
                 TripScreen(navController = navController, tripId = lastTripId.value )
             }
         }
         composable(route = BottomNavItem.TripCamera.screen_route,) {
             var lastTripId = LocalTripViewModel.current.lastTripId.collectAsState()
             if (lastTripId.value > 0){
                TripCameraScreen(navController = navController, tripId = lastTripId.value)
             }
         }
        composable(route = BottomNavItem.Home.screen_route) { backStackEntry ->
            LocalTripViewModel.current.onLoadingLastTrip()
            var lastTripId = LocalTripViewModel.current.numTrip.observeAsState()
            if (lastTripId.value == 0) {
                //Log.d("NavGraph/FlashScreen","Welcome screen")
                FlashScreen(navController = navController)
            }
            else {
                LocalTripViewModel.current.onLoadingLastTripId()
                var lastTripId = LocalTripViewModel.current.lastTripId.collectAsState()
                //Log.d("NavGraph/RecordScreen","{${lastTrip.value}}")
                if (lastTripId.value > 0){
                    RecordScreen(navController = navController,lastTripId.value)
                    LocalPhotoViewModel.current.onLoadPhotoByTripId(lastTripId.value)
                }

            }
        }
        composable(route = BottomNavItem.MyTrip.screen_route) { backStackEntry ->
            TripHistoryScreen(navController )
        }
        composable(route = BottomNavItem.Setting.screen_route) { backStackEntry ->
            SettingScreen(navController, backStackEntry,viewModel )
        }
        composable(route = BottomNavItem.PhotoDetail.screen_route+"/{path}",
            arguments = listOf(navArgument("path") {type = NavType.StringType},)) { backStackEntry ->
            PhotoScreen(navController = navController, navBackStackEntry = backStackEntry)
        }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.MyTrip,
        BottomNavItem.Setting,
    )
    androidx.compose.material.BottomNavigation(
        backgroundColor = MaterialTheme.colors.primary,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.imageVector, contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 9.sp) },
                selectedContentColor = MaterialTheme.colors.primaryVariant,
                unselectedContentColor = MaterialTheme.colors.primaryVariant.copy(0.4f),
                alwaysShowLabel = true,
                selected = currentRoute == item.screen_route,
                onClick = {
                    navController.navigate(item.screen_route) {
                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
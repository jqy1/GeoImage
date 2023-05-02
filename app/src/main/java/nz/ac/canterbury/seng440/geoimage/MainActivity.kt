package nz.ac.canterbury.seng440.geoimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.geoimage.service.share.NearbyShare
import nz.ac.canterbury.seng440.geoimage.ui.navigation.BottomNavigation
import nz.ac.canterbury.seng440.geoimage.ui.navigation.SetupNavGraph
import nz.ac.canterbury.seng440.geoimage.ui.theme.FABMenuBar_HeightOfMenuBar
import nz.ac.canterbury.seng440.geoimage.ui.theme.FABMenuBar_HeightOfMenuBar_elevation
import nz.ac.canterbury.seng440.geoimage.ui.theme.GeoImageTheme
import nz.ac.canterbury.seng440.geoimage.utils.PhotoViewModel
import nz.ac.canterbury.seng440.geoimage.utils.ScreenEvent
import nz.ac.canterbury.seng440.geoimage.utils.ShareAction
import nz.ac.canterbury.seng440.geoimage.utils.TripViewModel
import nz.ac.canterbury.seng440.geoimage.view.GeoImageViewModel
import nz.ac.canterbury.seng440.geoimage.view.GeoViewModelFactory
import nz.ac.canterbury.seng440.geoimage.view.PhotoViewModelFactory
import nz.ac.canterbury.seng440.geoimage.view.TripViewModelFactory


// Define a CompositionLocal global object with a default
// This instance can be accessed by all composables in the app
val LocalTripViewModel = compositionLocalOf { TripViewModel( GeoImageApp.instance.tripRepository ) }
val LocalPhotoViewModel = compositionLocalOf { PhotoViewModel( GeoImageApp.instance.photoRepository ) }

// We will move GeoViewModel into TripViewMode, RouteViewModel
val LocalGeoViewModel = compositionLocalOf { GeoImageViewModel( GeoImageApp.instance.geoRepository ) }

class MainActivity : ComponentActivity() {

    private val viewModel: GeoImageViewModel by viewModels() {
        GeoViewModelFactory((this.application as GeoImageApp).geoRepository)
    }

    private val tripViewModel: TripViewModel by viewModels() {
        TripViewModelFactory((this.application as GeoImageApp).tripRepository)
    }

    private val photoViewModel: PhotoViewModel by viewModels() {
        PhotoViewModelFactory((this.application as GeoImageApp).photoRepository)
    }

    private lateinit var localNearbyShare: NearbyShare


    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        localNearbyShare = NearbyShare(tripViewModel,"", this.applicationContext)

        setContent {
            GeoImageTheme {
                val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Closed))
                val scope = rememberCoroutineScope()
                val navController = rememberNavController()
                // Bind elevation as the value for LocalElevations
                CompositionLocalProvider(
                    LocalTripViewModel provides tripViewModel,
                    LocalGeoViewModel provides viewModel,
                    LocalPhotoViewModel provides photoViewModel,
                    ) {

                    Scaffold(
                        scaffoldState = scaffoldState,
                        topBar = { TopBar(scope = scope, scaffoldState = scaffoldState) },
                        bottomBar = { BottomNavigation(navController = navController) },
                        drawerContent = { DrawerView(scope = scope, scaffoldState = scaffoldState,navController=navController){
                            when(it.screenState){
                                is ScreenEvent.ScreeTripShareStart ->{
                                    localNearbyShare.startDiscovery()
                                }
                                is ScreenEvent.ScreeTripShareStop ->{
                                    localNearbyShare.stopAdvertising()
                                }
                            }
                        } },
                    ){ innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            SetupNavGraph(navController = navController, viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(scope: CoroutineScope, scaffoldState: ScaffoldState) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(Icons.Filled.Menu, "")
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onSurface
    )
}

@Composable
fun AddDrawerHeader(
    title: String,
    titleColor: Color = Color.Black,
) {
    Card(elevation = 4.dp, modifier = Modifier.fillMaxWidth(), border = BorderStroke(1.dp, color = Color.Gray),) {
        Text(text = title, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = titleColor), modifier = Modifier.padding(14.dp))

    }
}

@Composable
fun DrawerView(scope: CoroutineScope, scaffoldState: ScaffoldState, navController: NavController, onClick : (ShareAction<String>) -> Unit,) {
    val share = listOf("Enable", "Disable", )
    LazyColumn() {
        item {
            AddDrawerHeader(title = "Share")
        }
        items(share.size){index->
            AddDrawerContentView(title = share[index], selected = if (index==0)true else false,onClick)
        }
    }

}

@Composable
fun AddDrawerContentView(title: String, selected: Boolean, onClick : (ShareAction<String>) -> Unit,) {
    val enable = remember { mutableStateOf(false)  }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable {}.padding(horizontal = 16.dp, vertical = 12.dp),) {
        if (title.isNotEmpty()) {
            if (selected)
                Text(text = title, modifier = Modifier.weight(1f).clickable() {
                    enable.value = !enable.value
                    if(enable.value) {
                        var event : ShareAction<String>  = ShareAction<String>(
                            ScreenEvent.ScreeTripShareStart,"START")
                        onClick(event)
                    }else{
                        var event : ShareAction<String>  = ShareAction<String>(
                            ScreenEvent.ScreeTripShareStop,"STOP")
                        onClick(event)
                    }

                }, color = Color.Black,style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                ))
            else
                Text(text = title, modifier = Modifier.weight(1f),fontSize = 12.sp)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GeoImageTheme {
        //HomeScreen("test")
    }
}
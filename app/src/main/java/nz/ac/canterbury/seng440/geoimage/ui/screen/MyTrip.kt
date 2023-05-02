package nz.ac.canterbury.seng440.geoimage.ui.screen


import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.Payload
import nz.ac.canterbury.seng440.geoimage.*
import nz.ac.canterbury.seng440.geoimage.R
import nz.ac.canterbury.seng440.geoimage.model.Trip
import nz.ac.canterbury.seng440.geoimage.service.share.NearbyShare
import nz.ac.canterbury.seng440.geoimage.ui.component.*
import nz.ac.canterbury.seng440.geoimage.ui.theme.*
import nz.ac.canterbury.seng440.geoimage.utils.AddTrip
import nz.ac.canterbury.seng440.geoimage.utils.ScreenEvent
import nz.ac.canterbury.seng440.geoimage.utils.TripEvent
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getPathFromUri(uri: Uri, context: Context) : String{
    var s : Array<String> = Array(1,init = { MediaStore.Images.Media.DATA})
    val cursor = context.contentResolver.query(uri,s,null,null,null)
    if (cursor != null) {
        if(cursor.moveToFirst()){
            val column_index : Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            return cursor.getString(column_index)
        }
    }
    return ""
}



@Composable
fun TripDesc(trip:Trip, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(start = 16.dp, top = 8.dp, bottom = 16.dp, end = 16.dp)
    ) {
        Text(text = stringResource(R.string.trip_date), style = MaterialTheme.typography.h6)
        Row(modifier = modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)) {
            Text(
                text = datetimeFormat(trip.date) ,
                style = MaterialTheme.typography.body1
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = "",
                style = MaterialTheme.typography.body1
            )
            Text(
                text = datetimeFormat(trip.endDate),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TripPhotos(trip:Trip, modifier: Modifier = Modifier) {
    val mContext = LocalContext.current
    val viewPhotoViewModel = LocalPhotoViewModel.current
    viewPhotoViewModel.onLoadPhotoByTripId(trip.tripId)
    val photos = viewPhotoViewModel.photo.collectAsState()
    LazyVerticalGrid(
        modifier = Modifier.background(MaterialTheme.colors.background),
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        cells = GridCells.Adaptive(96.dp),
    ) {
        photos.value?.let { it->
            if(it.size>0){
                for( (k,v) in it) {
                    items(v){ photo->
                        run{
                            var file = GeoImageApp.instance.database.fileDao().findByPhotoId(photo.photoId)
                            file?.let {
                                val painter = rememberImagePainter( data = File(file.path))
                                Image(painter = painter,
                                    contentDescription = "",
                                    modifier= Modifier
                                        .size(96.dp)
                                        .padding(8.dp)
                                        .clip(RoundedCornerShape(10)),
                                    contentScale = ContentScale.FillWidth,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TripItem(trip: Trip, modifier: Modifier = Modifier,onClickMore : (TripEvent<Trip>) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        elevation = 4.dp,
        modifier = modifier.padding(8.dp)
    ) {
        Column(
            modifier = Modifier.animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow))
        ) {
            Row( modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ) {
                TripIcon(trip.tripId)
                TripInformation(trip)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {
                    var event : TripEvent<Trip>  = TripEvent<Trip>(
                        ScreenEvent.ScreeTripShareClicked,trip)
                    onClickMore(event)
                }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        tint = MaterialTheme.colors.secondary,
                        contentDescription = stringResource(R.string.expand_button_content_description),
                    )
                }
                TripItemButton(expanded = expanded, onClick = { expanded = !expanded },)
            }
            if (expanded) {
                TripDesc(trip)
                TripPhotos(trip)
            }
        }
    }
}

@Composable
private fun TripItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            tint = MaterialTheme.colors.secondary,
            contentDescription = stringResource(R.string.expand_button_content_description),
        )
    }
}

@Composable
fun TripIcon(tripId: Long, modifier: Modifier = Modifier) {
    var photo = GeoImageApp.instance.database.fileDao().getAllFilesByTripId(tripId)
    var painter = painterResource(R.drawable.test1)
    if(photo.size>0)
        painter = rememberImagePainter(data = java.io.File(photo[0].path))

    Image(modifier = modifier
        .size(96.dp)
        .padding(8.dp)
        .clip(RoundedCornerShape(50)),
        contentScale = ContentScale.Crop,
        painter = painter,
        contentDescription = null
    )
}

@Composable
fun TripInformation(trip: Trip, modifier: Modifier = Modifier) {
    Column {
        Text( text = trip.name, style = MaterialTheme.typography.h6,  modifier = modifier.padding(top = 24.dp))
        Text(
            text = trip.description,
            style = MaterialTheme.typography.body1
        )
    }
}

private fun datetimeFormat(date:String) : String {
    if(date.length==0)
        return ""
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    val dateTime = LocalDateTime.parse( date ,formatter)
    val formatterNew = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return dateTime.format(formatterNew)
}


@Composable
fun TripHistoryScreen(navController: NavController) {

    val viewModel = LocalGeoViewModel.current
    val tripViewModel = LocalTripViewModel.current
    val trips = tripViewModel.trips.observeAsState()
    val context = LocalContext.current
    val nearbyShare = NearbyShare(tripViewModel, "", context)
    val endPoints by tripViewModel.endPoints.observeAsState(initial = emptyList())
    val shareTrip = tripViewModel.shareTrip.collectAsState()

    Column(modifier = Modifier.background(MaterialTheme.colors.surface)) {
        GeoImageHeader()
        DialogAction(viewModel){it->
            when( it.screenState) {
                is ScreenEvent.ScreeTripShareClicked->{
                    viewModel.onEnableNearbyShare(true)
                }
                is ScreenEvent.ScreenTripDeleteClicked->{
                    shareTrip.value?.let {
                        tripViewModel.deleteTrip(it)
                        tripViewModel.onEnableDialog(false)

                    }
                }
            }
        }
        Column(modifier = Modifier.fillMaxHeight(), content = {
            trips.value?.let {
                if(it.size>0){
                    for( trip in it){
                        TripItem(trip){
                            viewModel.onOpenMyTipClicked(true)
                            tripViewModel.onLoadingLastTripId(it.t)
                        }
                    }
                }
            }
        })
        Spacer(Modifier.size(Card_Horizontal_Padding))
        DialogShareNearby(viewModel){
            when(it.screenState){
                is ScreenEvent.ScreeTripShareStart ->{
                    nearbyShare.startAdvertising()

                }
                is ScreenEvent.ScreeTripShareStop->{
                    // stop
                    nearbyShare.stopAdvertising()
                    makeToast(context, GeoImageApp.instance.getString(R.string.app_cancel_share) )
                }
                is ScreenEvent.ScreeTripShareSend->{
                    if(endPoints.size>0) {
                        shareTrip.value?.let {
                            var photos = GeoImageApp.instance.database.fileDao().getAllFilesByTripId(it.tripId)
                            nearbyShare.SendPhotos(endPoints,photos)
                        }

                    }
                }
            }
        }
        AddTrip(navController = navController)
    }
}



@Preview
@Composable
fun MyTripPreview() {

    val myTrips = listOf(
        Trip( 1,"University of Canterbufy","12/10/22 9AM","12/10/22 9PM" ),
        Trip(2,"City Center of ChCh","12/10/22 9AM","12/10/22 9PM"),
        Trip(3,"Lincoln","12/10/22 9AM","12/10/22 9PM"),
        Trip( 1,"University of Canterbufy","12/10/22 9AM","12/10/22 9PM" ),
        Trip(2,"City Center","12/10/22 9AM","12/10/22 9PM"),
        Trip(3,"Lincoln","12/10/22 9AM","12/10/22 9PM"),
        Trip( 1,"University of Canterbufy","12/10/22 9AM","12/10/22 9PM" ),
        Trip(2,"City Center","12/10/22 9AM","12/10/22 9PM"),
        Trip(3,"Lincoln","12/10/22 9AM","12/10/22 9PM"),
    )

    Column(modifier = Modifier.background(MaterialTheme.colors.surface)) {
        GeoImageHeader()
        Column(modifier = Modifier.weight(1f, fill = false), content = {
            for (myTrip in myTrips) {
                //MyTripCard(myTrip)
                TripItem(myTrip){

                }
            }
        })

        Spacer(Modifier.size(Card_Horizontal_Padding))
    }
}

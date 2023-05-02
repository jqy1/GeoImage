package nz.ac.canterbury.seng440.geoimage.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import nz.ac.canterbury.seng440.geoimage.LocalGeoViewModel
import nz.ac.canterbury.seng440.geoimage.LocalPhotoViewModel
import nz.ac.canterbury.seng440.geoimage.LocalTripViewModel
import nz.ac.canterbury.seng440.geoimage.R
import nz.ac.canterbury.seng440.geoimage.service.share.NearbyShare
import nz.ac.canterbury.seng440.geoimage.utils.ScreenEvent
import nz.ac.canterbury.seng440.geoimage.utils.ShareAction
import nz.ac.canterbury.seng440.geoimage.view.GeoImageViewModel

//https://www.youtube.com/watch?v=JZehdUU6VbQ


fun shareWeb(context:Context){
    val url = "https://www.youtube.com/watch?v=JZehdUU6VbQ"
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra("Share this",url)
    val chooser = Intent.createChooser(intent,"Share using.....")
    ContextCompat.startActivity(context, intent, null)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DevicesList(modifier: Modifier = Modifier){
    val viewPhotoViewModel = LocalPhotoViewModel.current

    val context = LocalContext.current
    val viewModel =LocalTripViewModel.current

    val photos = viewPhotoViewModel.photo.collectAsState()
    val endPoints = viewModel.endPoints.observeAsState()
    Card(
        elevation = 4.dp,
        modifier = modifier.padding(8.dp)
    ) {
        LazyVerticalGrid(
            modifier = Modifier.background(MaterialTheme.colors.background),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            cells = GridCells.Adaptive(128.dp),
        ) {
            endPoints.value?.let {
                items(it){device->
                    DevicesItem(device)
                }
            }

        }
    }

}

@Composable
fun DevicesItem(ennPoint: String = "Huawei", modifier: Modifier = Modifier){
    Row(){
        Image(modifier = modifier
            .size(48.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(50)),
            contentScale = ContentScale.Crop,
            painter = painterResource(R.drawable.test1),
            contentDescription = null
        )
        Text(
            modifier=modifier.padding(start=10.dp,top=10.dp),
            text = ennPoint,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun DialogShareNearby(viewModel: GeoImageViewModel, onClick : (ShareAction<String>) -> Unit,) {
    Column(modifier = Modifier.fillMaxHeight()) {
        var openAlertDialog = viewModel.enableNearbyShareDialog.collectAsState()

        if (openAlertDialog.value) {
            AlertDialog(
                modifier = Modifier
                    .background(MaterialTheme.colors.onBackground)
                    .height(400.dp),
                onDismissRequest = {
                    // Dismiss the dialog when the user clicks outside the dialog or on the back
                    // button. If you want to disable that functionality, simply use an empty
                    // onCloseRequest.
                    viewModel.onEnableNearbyShare(false)
                },
                title ={ Text( text = stringResource(R.string.app_trip_nearby_share) , style = MaterialTheme.typography.h6,) },
                text = {
                    SharePhotoNearbyPannel()
                },
                confirmButton = {

                    Button( onClick = {
                        var event : ShareAction<String>  = ShareAction<String>(
                            ScreenEvent.ScreeTripShareStart,"START")
                        onClick(event)

                    }) { Text( stringResource(R.string.app_start_share) )}
                },
                dismissButton = {
                    Button( onClick = {

                        var event : ShareAction<String>  = ShareAction<String>(
                            ScreenEvent.ScreeTripShareStop,"STOP")
                        onClick(event)

                    }) { Text( stringResource(R.string.app_cancel_share) )}
                    Button( onClick = {
                        var event : ShareAction<String>  = ShareAction<String>(
                            ScreenEvent.ScreeTripShareSend,"SEND")
                        onClick(event)

                    }) { Text( stringResource(R.string.app_start_share_send) )}
                }
            )
        }
    }
}


@Composable
fun SharePhotoNearbyPannel(modifier:Modifier=Modifier){
    val context = LocalContext.current
    val viewModel = LocalGeoViewModel.current

    val tripViewModel = LocalTripViewModel.current
    val shareTrip = tripViewModel.shareTrip.collectAsState()

    Column(modifier = Modifier.background(MaterialTheme.colors.background)) {
        DevicesList()

        Card(
            elevation = 4.dp,
            modifier = modifier.padding(8.dp)
        ){
            Column(){
                Text(
                    modifier=Modifier.padding(start = 8.dp, top = 8.dp),
                    text = stringResource(id = R.string.app_photos),
                    style = MaterialTheme.typography.h6
                )
                Spacer(
                    Modifier
                        .height(2.dp)
                        .background(MaterialTheme.colors.secondary))
                shareTrip.value?.let {
                    TripPhotos(trip = it)
                }
            }

        }

    }
}
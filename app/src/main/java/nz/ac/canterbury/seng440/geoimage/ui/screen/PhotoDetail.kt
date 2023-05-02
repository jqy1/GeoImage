package nz.ac.canterbury.seng440.geoimage.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import nz.ac.canterbury.seng440.geoimage.R
import nz.ac.canterbury.seng440.geoimage.ui.component.makeToast
import java.io.File
import java.net.URLEncoder
import java.net.URLDecoder
import kotlin.math.pow
import kotlin.math.sqrt


@Composable
fun PhotoViewer(navController: NavController, path:String){

    val size = remember {
        mutableStateOf(200)
    }

    val image = rememberImagePainter( data = File(path))
    Surface(modifier = Modifier
        .fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = image,contentDescription = "",
                modifier = Modifier
                    .size(size.value.dp)
                    .pointerInput(Unit) {
                        val fingerCount = 2
                        var previousDistance = 0f
                        forEachGesture {
                            awaitPointerEventScope {
                                do {
                                    val event = awaitPointerEvent()

                                    if (event.changes.size == fingerCount) {

                                        val firstX = event.changes[0].position.x
                                        val firstY = event.changes[0].position.y

                                        val secondX = event.changes[1].position.x
                                        val secondY = event.changes[1].position.y

                                        val currentDistance = sqrt(
                                            (secondX - firstX).pow(2) + (secondY - firstY).pow(2)
                                        )

                                        if (currentDistance - previousDistance > 10) {
                                            if (size.value < 500) {
                                                size.value += 3
                                            }
                                            previousDistance = currentDistance
                                        } else if (previousDistance - currentDistance > 10) {
                                            if (size.value > 50) {
                                                size.value -= 3
                                            }
                                            previousDistance = currentDistance
                                        }

                                    }
                                } while (event.changes.any {
                                        it.pressed
                                    })
                            }
                        }
                    })
        }
    }
}






@Composable
fun PhotoScreen(navController: NavController,navBackStackEntry: NavBackStackEntry){
    val context = LocalContext.current
    val path = navBackStackEntry.arguments?.getString("path");
    if(path!="{path}"){
        if (path != null) {
            var file = URLDecoder.decode(path,"UTF-8")
            PhotoViewer(navController,file)
        }
    }
    else{
        makeToast(context, stringResource(id = R.string.app_dialog_missing_parameter))
    }
    
}





@Preview(showBackground = true)
@Composable
fun PhotoDetailPreview(){
    //PhotoScreen(navController = rememberNavController(),"")
}
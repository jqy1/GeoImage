package nz.ac.canterbury.seng440.geoimage.ui.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import nz.ac.canterbury.seng440.geoimage.GeoImageApp
import nz.ac.canterbury.seng440.geoimage.LocalPhotoViewModel
import nz.ac.canterbury.seng440.geoimage.LocalTripViewModel
import nz.ac.canterbury.seng440.geoimage.R
import nz.ac.canterbury.seng440.geoimage.ui.theme.*
import java.io.File

@Preview
@Composable
fun GeoImageHeader(){
    val context = LocalContext.current
    val viewModel = LocalTripViewModel.current

    Row(modifier = Modifier
        .height(160.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterVertically)){
            Box(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = stringResource(R.string.app_ui_my_trip),
                    color = Black,
                    fontSize = FABText_SizeOfHeader,
                    fontWeight = FontWeight.Bold
                )
            }
            Box(modifier = Modifier.align(Alignment.CenterEnd)){
                Button(onClick = {
                    viewModel.onEnableDialog(true)
                }, modifier = Modifier
                    .padding(Button_Padding)
                    .size(Card_SizeOfActionIcon)
                    .clip(CircleShape),) {
                    Icon(
                        modifier = Modifier
                            .size(Card_SizeOfActionIcon)
                            .clip(CircleShape),
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Camera",
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun ImageResource(id:Long) {
    run{
        var file = GeoImageApp.instance.database.fileDao().findByPhotoId(id)
        file?.let {
            val painter = rememberImagePainter( data = File(file.path))
            Image(painter = painter,
                contentDescription = "",
                contentScale = ContentScale.FillWidth)
        }
    }
}

fun makeToast(context: Context, message: String = "invoked") {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}
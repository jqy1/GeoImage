package nz.ac.canterbury.seng440.geoimage.utils

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.location.Location
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileResults
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.FlipCameraAndroid
import androidx.compose.material.icons.outlined.Lens
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.exifinterface.media.ExifInterface
import androidx.navigation.NavController
import nz.ac.canterbury.seng440.geoimage.ui.theme.FABMenuBar_SizeOfActionIcon
import nz.ac.canterbury.seng440.geoimage.ui.theme.FABMenuBar_SizeOfBigActionIcon
import nz.ac.canterbury.seng440.geoimage.ui.theme.FABMenuBar_SpaceFromBoundary
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors


@Composable
fun CameraView(
    navController: NavController,
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val context = LocalContext.current
    var lensFacing by remember {
        mutableStateOf(CameraSelector.LENS_FACING_BACK)
    }
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) onImageCaptured(uri, true)
        }
    val location by LocationViewModel(context).getLocationLiveData().observeAsState()

    CameraPreviewView(imageCapture, lensFacing) { cameraUIAction ->
        when (cameraUIAction) {
            is CameraUIAction.OnCameraClick -> {
                if (location != null) imageCapture.takePicture(
                    context, lensFacing, location, onImageCaptured, onError
                )
                else Toast.makeText(context, "no location", Toast.LENGTH_SHORT).show()
            }

            is CameraUIAction.OnSwitchCameraClick -> {
                lensFacing = if (lensFacing != CameraSelector.LENS_FACING_FRONT) {
                    CameraSelector.LENS_FACING_FRONT
                } else {
                    CameraSelector.LENS_FACING_BACK
                }
            }
            is CameraUIAction.OnGalleryViewClick -> {
                galleryLauncher.launch("image/*")
            }
            is CameraUIAction.OnCloseCameraClick -> {
                navController.popBackStack()
            }
        }
    }

}

@Composable
private fun CameraPreviewView(
    imageCapture: ImageCapture,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    cameraUIAction: (CameraUIAction) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val cameraProviderFuture = remember(context) { ProcessCameraProvider.getInstance(context) }
    val cameraProvider = remember(cameraProviderFuture) { cameraProviderFuture.get() }

    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }
    LaunchedEffect(lensFacing) {
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, Modifier.fillMaxSize()) {

        }
        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.Bottom
        ) {
            CameraControlsRow(cameraUIAction)
        }

        Column(
            modifier = Modifier.align(Alignment.TopStart), verticalArrangement = Arrangement.Top
        ) {
            CameraControlIcon(iconModifier = Modifier
                .padding(FABMenuBar_SpaceFromBoundary)
                .size(FABMenuBar_SizeOfBigActionIcon),
                imageVector = Icons.Outlined.Close,
                contentDescription = "Close camera",
                onClick = { cameraUIAction(CameraUIAction.OnCloseCameraClick) })
        }
    }
}

@Composable
fun CameraControlsRow(cameraUIAction: (CameraUIAction) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(FABMenuBar_SpaceFromBoundary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CameraControlIcon(imageVector = Icons.Outlined.FlipCameraAndroid,
            contentDescription = "Change camera",
            iconModifier = Modifier.size(FABMenuBar_SizeOfActionIcon),
            onClick = { cameraUIAction(CameraUIAction.OnSwitchCameraClick) })
        CameraControlIcon(imageVector = Icons.Outlined.Lens,
            contentDescription = "Take photo",
            iconModifier = Modifier.size(FABMenuBar_SizeOfBigActionIcon),
            onClick = { cameraUIAction(CameraUIAction.OnCameraClick) })
        CameraControlIcon(imageVector = Icons.Outlined.PhotoLibrary,
            contentDescription = "Gallery",
            iconModifier = Modifier.size(FABMenuBar_SizeOfActionIcon),
            onClick = { cameraUIAction(CameraUIAction.OnGalleryViewClick) })

    }
}

@Composable
fun CameraControlIcon(
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = imageVector,
            modifier = iconModifier,
            contentDescription = contentDescription,
            tint = MaterialTheme.colors.onSurface
        )
    }
}

const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

private fun ImageCapture.takePicture(
    context: Context,
    lensFacing: Int,
    location: Location?,
    onImageCaptured: (Uri, Boolean) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val outputFileOptions = getOutputFileOptions(context, lensFacing)

    this.takePicture(outputFileOptions,
        Executors.newSingleThreadExecutor(),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: OutputFileResults) {
                outputFileResults.savedUri?.let { uri ->
                    // Write EXIF location to output file
                    val isLocationSaved = addLocationToFileForResult(context, uri, location)
                    onImageCaptured(uri, isLocationSaved)
                }
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception)
            }
        })
}

fun getRealPathFromURI(context: Context, contentURI: Uri): String? {
    val result: String?
    val cursor: Cursor? = context.contentResolver.query(contentURI, null, null, null, null)
    if (cursor == null) { // Source is Dropbox or other similar local file path
        result = contentURI.path
    } else {
        cursor.moveToFirst()
        val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        result = cursor.getString(idx)
        cursor.close()
    }
    return result
}


fun getOutputFileOptions(context: Context, lensFacing: Int): ImageCapture.OutputFileOptions {
    // Setup image capture metadata

    val metadata = ImageCapture.Metadata()
    metadata.isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT

    val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/GeoImage")
    }
    // Create output options object which contains file + metadata
    return ImageCapture.OutputFileOptions.Builder(
        context.contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues
    ).setMetadata(metadata).build()

}


fun addLocationToFileForResult(context: Context, uri: Uri, location: Location?): Boolean {
    val file = File(getRealPathFromURI(context, uri)!!)
    try {
        val exif = ExifInterface(file)
        exif.setGpsInfo(location)
        exif.saveAttributes()

        return ExifInterface(file).let {
            !it.getAttribute(ExifInterface.TAG_GPS_LATITUDE)
                .isNullOrBlank() || !it.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)
                .isNullOrBlank()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        return false
    }
}

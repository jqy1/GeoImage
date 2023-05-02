package nz.ac.canterbury.seng440.geoimage.utils

sealed class CameraUIAction {
    object OnCameraClick : CameraUIAction()
    object OnGalleryViewClick : CameraUIAction()
    object OnSwitchCameraClick : CameraUIAction()
    object OnCloseCameraClick : CameraUIAction()
}
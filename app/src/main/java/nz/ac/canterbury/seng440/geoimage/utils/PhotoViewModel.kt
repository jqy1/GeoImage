package nz.ac.canterbury.seng440.geoimage.utils


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.geoimage.model.File
import nz.ac.canterbury.seng440.geoimage.model.Route

class PhotoViewModel(private val photoeRepository: PhotoRepository) :ViewModel() {

    val _photos = MutableStateFlow( emptyMap<Route, List<File>>() )
    val photo: StateFlow<Map<Route, List<File>>> = _photos.asStateFlow()

    fun onLoadPhotoByTripId(tripId: Long) = viewModelScope.launch {
        if (tripId > 0) {
            _photos.value = photoeRepository.loadPhotoByTripId(tripId)
        }
    }

    val _lastPhoto = MutableStateFlow( File() )
    val lastPhoto: StateFlow<File> = _lastPhoto.asStateFlow()

    fun onLoadLastPhotoByTripId(tripId: Long) = viewModelScope.launch {
        if (tripId > 0) {
            _lastPhoto.value = photoeRepository.loadLastPhotoByTripId(tripId)
        }
    }

    /**
     * Operation of File management, such as add photo, video
     *
     * */
    fun addFile(file: File) = viewModelScope.launch {
        photoeRepository.insertFile(file)
        //TODO("Put extra add LocationPoint( logical code here, notice the ui, and etc")
    }

    fun updateFile(file: File) = viewModelScope.launch {
        photoeRepository.updateFile(file)
        //TODO("Put extra update LocationPoint( logical code here, for example, notice the ui, and etc")
    }

    fun deleteFile(file: File) = viewModelScope.launch {
        photoeRepository.deleteFile(file)
        //TODO("Put extra update LocationPoint( logical code here, for example, notice the ui, and etc")
    }

}
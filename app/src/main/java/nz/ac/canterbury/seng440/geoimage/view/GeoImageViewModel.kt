package nz.ac.canterbury.seng440.geoimage.view

import androidx.lifecycle.ViewModel
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


import nz.ac.canterbury.seng440.geoimage.data.repository.GeoImageRepository
import nz.ac.canterbury.seng440.geoimage.model.*
import nz.ac.canterbury.seng440.geoimage.utils.PhotoRepository
import nz.ac.canterbury.seng440.geoimage.utils.PhotoViewModel
import nz.ac.canterbury.seng440.geoimage.utils.TripRepository
import nz.ac.canterbury.seng440.geoimage.utils.TripViewModel
import java.util.ArrayList

class GeoImageViewModel(private val geoImageRepository: GeoImageRepository) : ViewModel() {

    // all course
    val routes: LiveData<List<Route>> = geoImageRepository.routes.asLiveData()
    val trips : LiveData<List<Trip>> = geoImageRepository.trips.asLiveData()
    var locationPoints : LiveData<List<LocationPoint>> = geoImageRepository.locationPoints.asLiveData()

    var pSend : String = ""

    private val _opSend = MutableStateFlow(false)
    val opSend: StateFlow<Boolean> = _opSend.asStateFlow()

    fun onSetSendFlag(enable:Boolean) {
        _opSend.value = enable
    }

    // endPoints for saving the discovery
    private val _endPointsLiveData = MutableLiveData<MutableList<String>>()
    init {
        _endPointsLiveData.value = ArrayList()
    }

    val endPoints: MutableLiveData<MutableList<String>>
        get() = _endPointsLiveData

    fun onAddEndPoint(endPoint:String){
        if(!_endPointsLiveData.value?.contains(endPoint)!!) {
            _endPointsLiveData.value?.add(endPoint)
            _endPointsLiveData.value = _endPointsLiveData.value
        }
    }

    /***
     * States management
     */
    // Initial value is false so the dialog is hidden
    private val _showTripCRUDDialog = MutableStateFlow(false)
    val showTripCRUDDialog: StateFlow<Boolean> = _showTripCRUDDialog.asStateFlow()

    fun onTripCRUDOperation(enable:Boolean) {
        _showTripCRUDDialog.value = enable
    }

    /***
     * States management
     */
    // Initial value is false so the dialog is hidden
    private val _showMyTripDialog = MutableStateFlow(false)
    val showMyTripDialog: StateFlow<Boolean> = _showMyTripDialog.asStateFlow()

    fun onOpenMyTipClicked(enable:Boolean) {
        _showMyTripDialog.value = enable
    }

    /***
     * States management
     */
    // Initial value is false so the dialog is hidden
    private val _enableNearbyShareDialog = MutableStateFlow(false)
    val enableNearbyShareDialog: StateFlow<Boolean> = _enableNearbyShareDialog.asStateFlow()

    fun onEnableNearbyShare(enable:Boolean) {
        _enableNearbyShareDialog.value = enable
    }


    /**
     * Operation of Trip management
     *
     * */
    fun addTrip(trip: Trip ) = viewModelScope.launch {
        geoImageRepository.insertTrip(trip)
        TODO("Put extra add trip logical code here, notice the ui, and etc")
    }

    fun updateTrip(trip: Trip ) = viewModelScope.launch {
        geoImageRepository.updateTrip(trip)
        TODO("Put extra update trip logical code here, for example, notice the ui, and etc")
    }

    fun deleteTrip(trip: Trip ) = viewModelScope.launch {
        geoImageRepository.deleteTrip(trip)
        TODO("Put extra update trip logical code here, for example, notice the ui, and etc")
    }

    fun getLastTripId() = viewModelScope.launch {
        geoImageRepository.getLastTripId()
        //TODO("Put extra update trip logical code here, for example, notice the ui, and etc")
    }


    /**
     * Operation of Route management
     *
     * */
    fun addRoute(route: Route) = viewModelScope.launch {
        geoImageRepository.insertRoute(route)
        TODO("Put extra add Route logical code here, notice the ui, and etc")
    }

    fun updateRoute(route: Route ) = viewModelScope.launch {
        geoImageRepository.updateRoute(route)
        TODO("Put extra update Route logical code here, for example, notice the ui, and etc")
    }

    fun deleteRoute(route: Route ) = viewModelScope.launch {
        geoImageRepository.deleteRoute(route)
        TODO("Put extra update Route logical code here, for example, notice the ui, and etc")
    }


    /**
     * Operation of location point management
     *
     * */
    fun addLocationPoint(locationPoint: LocationPoint) = viewModelScope.launch {
        geoImageRepository.insertLocationPoint(locationPoint)
        TODO("Put extra add LocationPoint( logical code here, notice the ui, and etc")
    }

    fun updateRoute(locationPoint: LocationPoint ) = viewModelScope.launch {
        geoImageRepository.updateLocationPoint(locationPoint)
        TODO("Put extra update LocationPoint( logical code here, for example, notice the ui, and etc")
    }

    fun deleteRoute(locationPoint: LocationPoint ) = viewModelScope.launch {
        geoImageRepository.deleteLocationPoint(locationPoint)
        TODO("Put extra update LocationPoint( logical code here, for example, notice the ui, and etc")
    }


    /**
     * Operation of File management, such as add photo, video
     *
     * */
    fun addFile(file: File) = viewModelScope.launch {
        geoImageRepository.insertFile(file)
        TODO("Put extra add LocationPoint( logical code here, notice the ui, and etc")
    }

    fun updateFile(file: File ) = viewModelScope.launch {
        geoImageRepository.updateFile(file)
        TODO("Put extra update LocationPoint( logical code here, for example, notice the ui, and etc")
    }

    fun deleteFile(file: File ) = viewModelScope.launch {
        geoImageRepository.deleteFile(file)
        TODO("Put extra update LocationPoint( logical code here, for example, notice the ui, and etc")
    }


    /**
     * Operation of File management, such as add photo, video
     *
     * */
    fun addUser(user: User ) = viewModelScope.launch {
        geoImageRepository.insertUser(user)
        TODO("Put extra add LocationPoint( logical code here, notice the ui, and etc")
    }

    fun updateFile(user: User ) = viewModelScope.launch {
        geoImageRepository.updateUser(user)
        TODO("Put extra update LocationPoint( logical code here, for example, notice the ui, and etc")
    }

    fun deleteFile(user: User ) = viewModelScope.launch {
        geoImageRepository.deleteUser(user)
        TODO("Put extra update LocationPoint( logical code here, for example, notice the ui, and etc")
    }

}

class GeoViewModelFactory(private val repository: GeoImageRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GeoImageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GeoImageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class TripViewModelFactory(private val repository: TripRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TripViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TripViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PhotoViewModelFactory(private val repository: PhotoRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PhotoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
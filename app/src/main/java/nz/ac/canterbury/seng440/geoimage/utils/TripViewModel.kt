package nz.ac.canterbury.seng440.geoimage.utils

import androidx.lifecycle.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.geoimage.model.Trip
import nz.ac.canterbury.seng440.geoimage.service.share.NearbyShare
import java.util.ArrayList

class TripViewModel(private val tripRepository: TripRepository ) : ViewModel(){

    val trips : LiveData<List<Trip>> = tripRepository.trips.asLiveData()
    val numTrip: LiveData<Int> = tripRepository.numTrip.asLiveData()

    var _lastTrip  = MutableStateFlow(Trip())
    val lastTrip: StateFlow<Trip> = _lastTrip.asStateFlow()

    var _shareTrip  = MutableStateFlow(Trip())
    val shareTrip: StateFlow<Trip> = _shareTrip.asStateFlow()


    fun onLoadingLastTripId(trip:Trip)  {
        _shareTrip.value = trip
    }


    private val _lastTripId = MutableStateFlow(0L)
    val lastTripId: StateFlow<Long> = _lastTripId.asStateFlow()

    fun onLoadingLastTripId() = viewModelScope.launch {
        _lastTripId.value = tripRepository.getLastTripId()
    }

    fun onLoadingLastTrip() = viewModelScope.launch {
        _lastTrip.value = tripRepository.getLastTrip()
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
    private val _enableDialog = MutableStateFlow(false)
    val enableDialog: StateFlow<Boolean> = _enableDialog.asStateFlow()

    fun onEnableDialog(enable:Boolean) {
        _enableDialog.value = enable
    }


    /**
     * Operation of Trip management
     *
     * */
    fun addTrip(trip: Trip) = viewModelScope.launch {
        tripRepository.insertTrip(trip)
        //TODO("Put extra add trip logical code here, notice the ui, and etc")
    }

    fun updateTrip(trip: Trip) = viewModelScope.launch {
        tripRepository.updateTrip(trip)
        //TODO("Put extra update trip logical code here, for example, notice the ui, and etc")
    }

    fun deleteTrip(trip: Trip) = viewModelScope.launch {
        tripRepository.deleteTrip(trip)
        //TODO("Put extra update trip logical code here, for example, notice the ui, and etc")
    }

    suspend fun getLastTripId() = viewModelScope.launch {
        tripRepository.getLastTripId()
    }

    suspend fun getLastTrip() = viewModelScope.launch {
        tripRepository.getLastTrip()
    }

}
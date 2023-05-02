package nz.ac.canterbury.seng440.geoimage.utils

import android.content.Context
import androidx.lifecycle.ViewModel

class LocationViewModel(context: Context): ViewModel() {

    private val locationLiveData = LocationLiveData(context = context)

    fun getLocationLiveData(): LocationLiveData {
        return locationLiveData
    }

    fun startLocationUpdates() {
        locationLiveData.startLocationUpdate()
    }
}
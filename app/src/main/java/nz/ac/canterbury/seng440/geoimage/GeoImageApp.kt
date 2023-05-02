package nz.ac.canterbury.seng440.geoimage

import android.app.Application
import nz.ac.canterbury.seng440.geoimage.data.GeoImageDatabase
import nz.ac.canterbury.seng440.geoimage.data.repository.GeoImageRepository
import nz.ac.canterbury.seng440.geoimage.utils.PhotoRepository
import nz.ac.canterbury.seng440.geoimage.utils.TripRepository

// a single install based on design patten
class GeoImageApp :Application() {

    val database by lazy { GeoImageDatabase.getDatabase(this) }

    val geoRepository by lazy {
        GeoImageRepository(database.fileDao(),
                           database.locationPointDao(),
                           database.routeDao(),
                           database.tripDao(),
                           database.userDao(),)
    }

    val tripRepository by lazy {
        TripRepository(database.tripDao(),)
    }

    val photoRepository by lazy {
        PhotoRepository(database.fileDao(),)
    }

    companion object {
        lateinit var instance: GeoImageApp private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}


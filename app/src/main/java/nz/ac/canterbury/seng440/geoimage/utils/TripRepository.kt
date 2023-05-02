package nz.ac.canterbury.seng440.geoimage.utils

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import nz.ac.canterbury.seng440.geoimage.data.trips.impl.TripDAO
import nz.ac.canterbury.seng440.geoimage.model.Trip

class TripRepository(private val tripDAO: TripDAO,) {
    /**
     *  start Data Storage for Trip, all the user code block put here
     *
     * */
    var trips : Flow<List<Trip>> = tripDAO.loadAll()
    val numTrip : Flow<Int> = tripDAO.getCount()


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertTrip(trip: Trip) {
        Log.d("D/insertTrip",trip?.toString())
        tripDAO.insert(trip)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateTrip(trip: Trip) {
        Log.d("D/updateTrip",trip?.toString())
        tripDAO.update(trip)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteTrip(trip: Trip) {
        Log.d("D/deleteTrip",trip?.toString())
        tripDAO.delete(trip)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getLastTripId(): Long {
        return tripDAO.getLastTripId()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getLastTrip(): Trip {
        return tripDAO.getLastTrip()
    }
}
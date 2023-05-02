package nz.ac.canterbury.seng440.geoimage.data.trips.impl

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nz.ac.canterbury.seng440.geoimage.model.Trip

@Dao
interface TripDAO {

    @Query("SELECT * FROM Trip")
    fun getAll(): List<Trip>

    @Query("SELECT * FROM Trip ORDER BY tripId DESC")
    fun loadAll(): Flow<List<Trip>>

    @Query("SELECT tripId FROM Trip order by tripId desc limit 1")
    fun getLastTripId() : Long


    @Query("SELECT * FROM Trip order by tripId desc limit 1")
    fun getLastTrip() : Trip

    @Query("SELECT count(tripId) FROM Trip")
    fun getCount() : Flow<Int>


    @Query("SELECT * FROM Trip WHERE tripId = (:tripId)  LIMIT 1")
    fun findByName( tripId: Long ): Trip

    @Query("SELECT * FROM Trip WHERE tripId = (:tripId)  LIMIT 1")
    fun getAllFilesByTripId(tripId : Long ): Trip

    @Insert
    fun insert(trip: Trip): Long

    @Update
    fun update(trip: Trip)

    @Delete
    fun delete(trip: Trip)


}
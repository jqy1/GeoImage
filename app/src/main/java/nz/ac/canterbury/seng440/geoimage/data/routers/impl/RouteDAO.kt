package nz.ac.canterbury.seng440.geoimage.data.routers.impl

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nz.ac.canterbury.seng440.geoimage.model.Route

@Dao
interface RouteDAO {

    @Query("SELECT * FROM Route")
    fun getAll(): List<Route>

    @Query("SELECT * FROM Route")
    fun loadAll(): Flow<List<Route>>


    @Query("SELECT count(routId) FROM Route")
    fun getCount() : Flow<Int>


    @Query("SELECT * FROM Route WHERE routID = (:routeId)  LIMIT 1")
    fun findByName(routeId: Long ): Route

    @Query("SELECT * FROM Route WHERE tripId = (:tripId)")
    fun getAllFilesByTripId(tripId : Long ): Route

    @Insert
    fun insert(route: Route): Long

    @Update
    fun update(route: Route)

    @Delete
    fun delete(route: Route)

}
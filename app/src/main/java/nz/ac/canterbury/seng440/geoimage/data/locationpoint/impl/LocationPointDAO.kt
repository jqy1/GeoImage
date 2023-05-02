package nz.ac.canterbury.seng440.geoimage.data.locationpoint.impl

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nz.ac.canterbury.seng440.geoimage.model.LocationPoint

@Dao
interface LocationPointDAO {

    @Query("SELECT * FROM LocationPoint")
    fun getAll(): List<LocationPoint>

    @Query("SELECT * FROM LocationPoint")
    fun loadAll(): Flow<List<LocationPoint>>


    @Query("SELECT count(locationPointID) FROM LocationPoint")
    fun getCount() : Flow<Int>


    @Query("SELECT * FROM LocationPoint WHERE locationPointID = (:locationPoint)  LIMIT 1")
    fun findByName(locationPoint : Long ): LocationPoint

    @Insert
    fun insert(locationPoint: LocationPoint): Long

    @Update
    fun update(locationPoint: LocationPoint)

    @Delete
    fun delete(locationPoint: LocationPoint)

}
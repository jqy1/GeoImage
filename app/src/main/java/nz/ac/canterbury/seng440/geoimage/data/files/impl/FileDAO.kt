package nz.ac.canterbury.seng440.geoimage.data.files.impl
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nz.ac.canterbury.seng440.geoimage.model.File
import nz.ac.canterbury.seng440.geoimage.model.Route

@Dao
interface FileDAO {

    @Query("SELECT * FROM File")
    fun getAll(): List<File>

    @Query("SELECT * FROM File")
    fun loadAll(): Flow<List<File>>

    @Query("SELECT * FROM File INNER JOIN Route on File.routeId==Route.routId  where tripId=(:tripId)")
    fun loadPhotosByTripId(tripId:Long): Map<Route, List<File>>

    @Query("SELECT * FROM File INNER JOIN Route on File.routeId==Route.routId where tripId=(:tripId) ORDER BY date desc LIMIT 1  ")
    fun loadLastPhotoByTripId(tripId:Long): File



    @Query("SELECT count(photoId) FROM File")
    fun getCount() : Flow<Int>


    @Query("SELECT * FROM File WHERE photoId = (:photoId)  LIMIT 1")
    fun findByPhotoId(photoId : Long ): File

    @Query("SELECT * FROM File WHERE routeId = (:routeId)  LIMIT 1")
    fun getAllFilesByRouteId(routeId : Long ): File

    @Query("SELECT File.* FROM File INNER JOIN Route on File.routeId==Route.routId where tripId=(:tripId) ORDER BY date")
    fun getAllFilesByTripId(tripId : Long ): List<File>

    @Insert
    fun insert(file: File): Long

    @Update
    fun update(file: File)

    @Delete
    fun delete(file: File)

}
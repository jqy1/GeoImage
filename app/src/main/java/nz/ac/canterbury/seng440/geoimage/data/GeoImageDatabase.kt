package nz.ac.canterbury.seng440.geoimage.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import nz.ac.canterbury.seng440.geoimage.data.files.impl.FileDAO
import nz.ac.canterbury.seng440.geoimage.data.locationpoint.impl.LocationPointDAO
import nz.ac.canterbury.seng440.geoimage.data.routers.impl.RouteDAO
import nz.ac.canterbury.seng440.geoimage.data.trips.impl.TripDAO
import nz.ac.canterbury.seng440.geoimage.data.users.impl.UserDAO
import nz.ac.canterbury.seng440.geoimage.model.*

@Database(
    entities = [User::class, Route::class, File::class, LocationPoint::class, Trip::class],
    version = 1,
    exportSchema = false
)
abstract class GeoImageDatabase : RoomDatabase() {

    abstract  fun userDao() : UserDAO

    abstract fun tripDao(): TripDAO

    abstract fun routeDao() : RouteDAO

    abstract fun locationPointDao() : LocationPointDAO

    abstract fun fileDao() : FileDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: GeoImageDatabase? = null

        fun getDatabase(context: Context): GeoImageDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GeoImageDatabase::class.java,
                    "geo_image_database"
                ).apply {


                }.allowMainThreadQueries().build()// .allowMainThreadQueries()
                //).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}


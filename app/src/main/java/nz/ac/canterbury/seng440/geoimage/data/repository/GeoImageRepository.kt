package nz.ac.canterbury.seng440.geoimage.data.repository
import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import nz.ac.canterbury.seng440.geoimage.data.files.impl.FileDAO
import nz.ac.canterbury.seng440.geoimage.data.locationpoint.impl.LocationPointDAO
import nz.ac.canterbury.seng440.geoimage.data.routers.impl.RouteDAO
import nz.ac.canterbury.seng440.geoimage.data.trips.impl.TripDAO
import nz.ac.canterbury.seng440.geoimage.data.users.impl.UserDAO
import nz.ac.canterbury.seng440.geoimage.model.*


class GeoImageRepository(private  val fileDAO: FileDAO,
                         private  val locationPointDAO: LocationPointDAO ,
                         private val routeDAO: RouteDAO,
                         private val tripDAO: TripDAO,
                         private val userDAO: UserDAO,) {

    val locationPoints : Flow<List<LocationPoint>> = locationPointDAO.loadAll()
    val numLocationPoints : Flow<Int> = locationPointDAO.getCount()

    var files : Flow<List<File>> = fileDAO.loadAll()
    val numFile : Flow<Int> = fileDAO.getCount()

    var trips : Flow<List<Trip>> = tripDAO.loadAll()
    val numTrip : Flow<Int> = tripDAO.getCount()

    var routes : Flow<List<Route>> = routeDAO.loadAll()
    val numRoute : Flow<Int> = routeDAO.getCount()

    var users : Flow<List<User>> = userDAO.loadAll()
    val numUser : Flow<Int> = userDAO.getCount()


    /**
     *  start Data Storage for Trip, all the user code block put here
     *
     * */

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
    suspend fun getLastTripId(): () -> Long {
        return {
            tripDAO.getLastTripId()
        }
    }


    /**
     *  start Data Storage for location point, all the user code block put here
     *
     * */


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertLocationPoint(locationPoint: LocationPoint) {
        Log.d("D/insertLocationPoint",locationPoint?.toString())
        locationPointDAO.insert(locationPoint)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateLocationPoint(locationPoint: LocationPoint) {
        Log.d("D/updateLocationPoint",locationPoint?.toString())
        locationPointDAO.update(locationPoint)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteLocationPoint(locationPoint: LocationPoint) {
        Log.d("D/deleteLocationPoint",locationPoint?.toString())
        locationPointDAO.delete(locationPoint)
    }

    /**
     *  start Data Storage for Files, all the user code block put here
     *
     * */


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertFile(file: File) {
        Log.d("D/insertFile",file?.toString())
        fileDAO.insert(file)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateFile(file: File) {
        Log.d("D/updateFile",file?.toString())
        fileDAO.update(file)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteFile(file: File) {
        Log.d("D/deleteFile",file?.toString())
        fileDAO.delete(file)
    }


    /**
     *  start Data Storage for Route, all the user code block put here
     *
     * */

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertRoute(route: Route) {
        Log.d("D/insertRoute",route?.toString())
        routeDAO.insert(route)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateRoute(route: Route) {
        Log.d("D/updateRoute",route?.toString())
        routeDAO.update(route)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteRoute(route: Route) {
        Log.d("D/deleteRoute",route?.toString())
        routeDAO.delete(route)
    }


    /**
     *  start Data Storage for User, all the user code block put here
     *
     * */

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertUser(user: User) {
        Log.d("D/insertUser",user?.toString())
        userDAO.insert(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateUser(user: User) {
        Log.d("D/updateUser",user?.toString())
        userDAO.update(user)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteUser(user: User) {
        Log.d("D/deleteUser",user?.toString())
        userDAO.delete(user)
    }

    /**
     *  end Data Storage for User
     *
     * */

}
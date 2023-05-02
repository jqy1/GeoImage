package nz.ac.canterbury.seng440.geoimage.utils

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import nz.ac.canterbury.seng440.geoimage.data.routers.impl.RouteDAO
import nz.ac.canterbury.seng440.geoimage.model.Route

class RouteRepository(private val routeDAO: RouteDAO) {
    /**
     *  start Data Storage for Route, all the user code block put here
     *
     * */

    var routes : Flow<List<Route>> = routeDAO.loadAll()
    val numRoute : Flow<Int> = routeDAO.getCount()

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
}
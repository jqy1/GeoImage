package nz.ac.canterbury.seng440.geoimage.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import nz.ac.canterbury.seng440.geoimage.model.LocationPoint
import nz.ac.canterbury.seng440.geoimage.model.Route

class RouteViewModel(private val routeRepository: RouteRepository, ) :ViewModel(){



    /**
     * Operation of Route management
     *
     * */
    fun addRoute(route: Route) = viewModelScope.launch {
        routeRepository.insertRoute(route)
        TODO("Put extra add Route logical code here, notice the ui, and etc")
    }

    fun updateRoute(route: Route) = viewModelScope.launch {
        routeRepository.updateRoute(route)
        TODO("Put extra update Route logical code here, for example, notice the ui, and etc")
    }

    fun deleteRoute(route: Route) = viewModelScope.launch {
        routeRepository.deleteRoute(route)
        TODO("Put extra update Route logical code here, for example, notice the ui, and etc")
    }


}
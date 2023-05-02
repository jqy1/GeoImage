package nz.ac.canterbury.seng440.geoimage.utils

sealed class ScreenEvent {
    object ScreenTripAddClicked : ScreenEvent()
    object ScreenTripEditClicked : ScreenEvent()
    object ScreenTripDeleteClicked : ScreenEvent()

    object ScreeTripShareClicked : ScreenEvent()
    object ScreeTripShareStart : ScreenEvent()
    object ScreeTripShareStop : ScreenEvent()
    object ScreeTripShareSend : ScreenEvent()
}

sealed class Event<T>(screenState:ScreenEvent?,t:T?) {
    var value = t
    var state = screenState
}

/*
* define model class to pass through event (source) and data in MVVM
*
* */
data class TripEvent<Trip>(val screenState:ScreenEvent, val t:Trip) :
    Event<Trip>(screenState= screenState, t = t )

data class RouteEvent<Route>(val screenState:ScreenEvent, val t:Route) :
    Event<Route>(screenState= screenState, t = t )

data class LocationPointEvent<LocationPoint>(val screenState:ScreenEvent, val t: LocationPoint) :
    Event<LocationPoint>(screenState= screenState, t = t )


data class ShareAction<String>(val screenState: ScreenEvent, val t: String) :
    Event<String>(screenState= screenState, t = t )

data class SaveAction<String>(val screenState:ScreenEvent, val t: String) :
    Event<String>(screenState= screenState, t = t )
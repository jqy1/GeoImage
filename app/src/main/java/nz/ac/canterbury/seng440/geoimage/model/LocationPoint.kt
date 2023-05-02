package nz.ac.canterbury.seng440.geoimage.model

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
class LocationPoint {

    @ColumnInfo
    var longitude: Double = 0.0

    @ColumnInfo
    var latitude: Double = 0.0

    @ColumnInfo
    var routeId: Long = 0

    @PrimaryKey(autoGenerate = true)
    var locationPointID: Long = 0

    constructor() {}

    //for add
    @Ignore
    constructor(latitude: Double, longitude: Double, routeId: Long) {
        this.latitude = latitude
        this.longitude = longitude
        this.routeId = routeId
        //this.date =
    }

    //for add
    @Ignore
    constructor(location: Location, routeId: Long) {
        this.latitude = location.latitude
        this.longitude = location.longitude
        this.routeId = routeId
        //this.date =
    }


    // for update
    @Ignore
    constructor(id: Long, latitude: Double, longitude: Double, routeId: Long) {
        this.locationPointID = id

        this.longitude = longitude
        this.latitude = latitude
        this.routeId = routeId
    }

    //Just use location in general
    @Ignore
    constructor(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
    }
}
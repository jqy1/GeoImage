package nz.ac.canterbury.seng440.geoimage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Entity
class Route{

    @ColumnInfo
    var tripId: Long = 0 // FK from Trip table

    @ColumnInfo
    var description : String = "" // description of the route

    @ColumnInfo
    var tag : String = ""  // tag of the route, a string can separated by comma, eg: Happy, fun, cloud, cold,

    @ColumnInfo
    var createdDate :String = "" // create date of this recorder, automatically generate by app

    @ColumnInfo
    var updateDate : String = "" // update date of this recorder, automatically generate by app

    @PrimaryKey(autoGenerate = true)
    var routId: Long = 0

    private fun setCurrentDate(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        this.createdDate = current.format(formatter)
    }

    constructor(){}

    //for add
    @Ignore
    constructor(tripId: Long, description: String, tag:String,) {
        this.tripId = tripId
        this.description = description
        this.tag = tag

        setCurrentDate()
        //this.date =
    }
    // for update
    @Ignore
    constructor(id:Long, tripId: Long, description: String, tag:String,) {
        this.routId = id

        this.tripId = tripId
        this.description = description
        this.tag = tag

        setCurrentDate()
    }


}
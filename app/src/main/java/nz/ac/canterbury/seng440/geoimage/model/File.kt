package nz.ac.canterbury.seng440.geoimage.model
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

//*photoId : number <<generated>>
//--
//*routeId : number <<FK>>
//--
//* locationPointId <<FK>>
//--
//description : text
//tag   : text
//type  : file types ( photo, video, text, doc, and etc)
//path  : text

//*createdDate : datetime, automatically recorder by app
//*updateDate : datetime, automatically recorder by app


@Entity
class File {

    @PrimaryKey(autoGenerate = true)
    var photoId : Long = 0

    @ColumnInfo
    var routeId : Long = 0
    @ColumnInfo
    var locationPointId : Long = 0

    @ColumnInfo
    var description : String = ""
    @ColumnInfo
    var type : String = ""
    @ColumnInfo
    var tag : String = ""
    @ColumnInfo
    var path : String = ""
    @ColumnInfo
    var uri : String = ""
    @ColumnInfo
    var date :String = ""

    constructor(){}

    private fun setCurrentDate(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        this.date = current.format(formatter)
    }

    //for add
    @Ignore
    constructor(routeId: Long, locationPointId : Long, description : String, tag:String, path:String, uri:String) {

        this.routeId = routeId
        this.locationPointId = locationPointId
        this.description = description
        this.tag = tag
        this.path = path
        this.uri = uri
        this.type = "content"

        setCurrentDate()

        //this.date =
    }

    // for update
    @Ignore
    constructor(id:Long, routeId: Long, locationPointId : Long, description : String, tag:String, path:String, uri:String){
        this.photoId = id

        this.routeId = routeId
        this.locationPointId = locationPointId
        this.description = description
        this.tag = tag
        this.path = path
        this.uri = uri

        setCurrentDate()
    }

}
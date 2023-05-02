package nz.ac.canterbury.seng440.geoimage.model
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@Entity
class Trip{
    @ColumnInfo
    var userId: String=""

    @ColumnInfo
    var name: String =""

    @ColumnInfo
    var date: String = ""

    @ColumnInfo
    var endDate : String = ""

    @ColumnInfo
    var description:String  =""

    @ColumnInfo
    var tag:String=""

    @PrimaryKey(autoGenerate = true)
    var tripId: Long = 0

    private fun setCurrentDate(){
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        this.date = current.format(formatter)
    }

    constructor(){}

    //for add
    @Ignore
    constructor(name: String, description: String,tag:String,)  {
        this.name = name
        this.description = description
        this.tag = tag
        setCurrentDate()
    }

    // for update
    @Ignore
    constructor(id:Long, name: String, description: String,tag:String,){
        this.name = name
        this.description = description
        this.tag = tag
        this.tripId = id
        setCurrentDate()
    }

    override fun toString() = "{$tripId}-{$name}-{$description}-{$tag}"
}


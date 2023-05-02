package nz.ac.canterbury.seng440.geoimage.model

import androidx.room.Ignore

class Setting {
    var gpsMethod : String =""
    var mapType : String = ""
    var language : String = ""

    constructor(){}

    //for add
    @Ignore
    constructor(gpsMethod: String, mapType: String, language:String,) {
        this.gpsMethod = gpsMethod
        this.mapType = mapType
        this.language = language
    }
}
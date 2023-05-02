package nz.ac.canterbury.seng440.geoimage.model

import android.graphics.drawable.Drawable
import androidx.room.Ignore

class AppInfo {
    var pkgName:String = ""
    var launchClassName:String = ""
    var appName:String = "";
    lateinit var appIcon: Drawable

    constructor(){}

    //for add
    @Ignore
    constructor(pkgName:String, launchClassName:String , appName:String, appIcon: Drawable, ) {
        this.pkgName = pkgName
        this.launchClassName = launchClassName
        this.appName = appName
        this.appIcon = appIcon
    }
}
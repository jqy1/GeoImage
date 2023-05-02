package nz.ac.canterbury.seng440.geoimage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date

// *userId : number <<generated>>
// * password : text, encyrptoing by md5 or other.
//--
// *nickName : text
// *mail     : text
// *date : datetime, automatically recorder by app
@Entity
class User{

    @PrimaryKey
    var userID: String = "AA"

    @ColumnInfo
    var password : String = ""

    @ColumnInfo
    var nickName : String = ""

    @ColumnInfo
    var mail : String = ""

    @ColumnInfo
    var date : String = ""

    constructor(){}

    //for add
    @Ignore
    constructor(nickName: String, password: String, mail:String,){
        this.nickName = nickName
        this.password = password
        this.mail = mail
        this.userID = ""
    }

    // for update
    @Ignore
    constructor(userId: String, nickName: String, password: String, mail:String,){
        this.userID = userId

        this.nickName = nickName
        this.password = password
        this.mail = mail
    }

    override fun toString() = "{$userID}-{$nickName}-{$mail}"
}
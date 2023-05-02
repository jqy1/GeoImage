package nz.ac.canterbury.seng440.geoimage

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import java.math.BigInteger
import java.security.MessageDigest

import org.junit.Assert.*
import nz.ac.canterbury.seng440.geoimage.data.GeoImageDatabase
import nz.ac.canterbury.seng440.geoimage.model.File
import nz.ac.canterbury.seng440.geoimage.model.Route
import nz.ac.canterbury.seng440.geoimage.model.Trip
import nz.ac.canterbury.seng440.geoimage.model.User
import java.util.concurrent.Flow

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("nz.ac.canterbury.seng440.geoimage", appContext.packageName)
    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    @Test
    fun addUser(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        var db = GeoImageDatabase.getDatabase(appContext)
        val userDao = db.userDao()

        var user1: User = User(md5("AA-Jack"),"Jack","123456","Jack@pg.canterbury.ac.nz")
        userDao.insert(user1)
        var user2: User = User(md5("AA-Ben"),"Ben","Nick","ben@canterbury.ac.nz")
        userDao.insert(user2)
        var user3: User = User(md5("AA-Nicel"),"Nicel","Goodman","nicel@canterbury.ac.nz")
        userDao.insert(user3)

        val users: List<User> = userDao.getAll()
        Log.d("T",users.toString())
    }

    @Test
    fun listUser(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        var db = GeoImageDatabase.getDatabase(appContext)
        val userDao = db.userDao()
        val users: List<User> = userDao.getAll()
        Log.d("T/users",users.toString())
    }

    @Test
    fun addTrips(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        var db = GeoImageDatabase.getDatabase(appContext)
        val tirpDao = db.tripDao()

        var trip1 = Trip("Canterbury","Day trip of canterbury","One Day trip, Canterbury")
        tirpDao.insert(trip1)

    }

    @Test
    fun listTrips(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        var db = GeoImageDatabase.getDatabase(appContext)
        val tirpDao = db.tripDao()

        val trips: List<Trip> = tirpDao.getAll()
        Log.d("T/Trips",trips.toString())
    }

    fun addRoutes(){

    }
    @Test
    fun listRoutes(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        var db = GeoImageDatabase.getDatabase(appContext)
        val routeDao = db.routeDao()

        val routes: List<Route> = routeDao.getAll()
        Log.d("T/Routes",routes.toString())
    }

    fun addLocationPoint(){

    }
    @Test
    fun listLocationPoint(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        var db = GeoImageDatabase.getDatabase(appContext)
        val fileDao = db.fileDao()

        val files: List<File> = fileDao.getAll()
        Log.d("T/Photos",files.toString())
    }


}
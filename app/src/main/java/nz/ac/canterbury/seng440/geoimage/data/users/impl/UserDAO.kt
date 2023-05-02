package nz.ac.canterbury.seng440.geoimage.data.users.impl

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import nz.ac.canterbury.seng440.geoimage.model.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM User")
    fun getAll(): List<User>

    @Query("SELECT * FROM User")
    fun loadAll(): Flow<List<User>>


    @Query("SELECT count(userID) FROM User")
    fun getCount() : Flow<Int>


    @Query("SELECT * FROM User WHERE userID = (:userId)  LIMIT 1")
    fun findByName( userId: String ): User

    @Insert
    fun insert(user: User): Long

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)


}
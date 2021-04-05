package sidev.app.course.dicoding.bab3_modul3.appcommon.db

import androidx.room.*
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.User

@Dao
interface UserFavDao {
    @Query("SELECT * FROM user_fav WHERE username = :username LIMIT 1")
    fun getUname(username: String): List<User>

    @Query("SELECT * FROM user_fav WHERE username LIKE :username")
    fun searchUname(username: String): List<User>

    @Query("SELECT * FROM user_fav")
    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    fun delete(username: String) = delete(
        getUname(username).first()
    )

    fun isFav(username: String): Boolean = try {
        getUname(username)
        true
    } catch (e: Exception){
        false
    }
}
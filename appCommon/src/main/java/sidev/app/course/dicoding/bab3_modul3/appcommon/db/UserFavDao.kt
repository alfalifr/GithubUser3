package sidev.app.course.dicoding.bab3_modul3.appcommon.db

import androidx.room.*
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.User
import sidev.lib.android.std.tool.util.`fun`.loge

@Dao
interface UserFavDao {
    @Query("SELECT * FROM user_fav WHERE username = :username")
    fun getUname(username: String): User?

    @Query("SELECT * FROM user_fav WHERE username LIKE :username")
    fun searchUname(username: String): List<User>

    @Query("SELECT * FROM user_fav")
    fun getAll(): List<User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    fun delete(username: String): Boolean = getUname(username).let {
        if(it == null) false else {
            delete(it)
            true
        }
    }

    fun isFav(username: String): Boolean = getUname(username) != null
}
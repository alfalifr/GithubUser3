package sidev.app.course.dicoding.bab3_modul3.appcommon.db

import android.content.ContentValues
import android.database.Cursor
import androidx.room.*
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.User
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Util
import sidev.lib.android.std.tool.util.`fun`.loge
import java.lang.IllegalStateException

@Dao
interface UserFavDao {
    @Query("SELECT * FROM user_fav WHERE username = :username")
    fun getByUname(username: String): User?

    @Query("SELECT * FROM user_fav WHERE username = :username")
    fun getCursorByUname(username: String): Cursor

    @Query("SELECT * FROM user_fav WHERE username LIKE :username")
    fun searchUname(username: String): List<User>

    @Query("SELECT * FROM user_fav WHERE username LIKE :username")
    fun searchCursorUname(username: String): Cursor

    @Query("SELECT * FROM user_fav")
    fun getAll(): List<User>

    @Query("SELECT * FROM user_fav")
    fun getAllCursor(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    fun insert(values: ContentValues) = insert(
        User(
            values.getAsString(Const.PROP_USERNAME),
            values.getAsString(Const.PROP_AVATAR),
        )
    )

    @Delete
    fun delete(user: User): Int

    fun delete(username: String): Boolean = getByUname(username).let {
        if(it == null) false else delete(it) > 0
    }

    fun isFav(username: String): Boolean = getByUname(username) != null
}
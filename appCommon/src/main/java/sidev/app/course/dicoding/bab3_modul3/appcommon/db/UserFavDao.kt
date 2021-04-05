package sidev.app.course.dicoding.bab3_modul3.appcommon.db

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.room.*
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.User
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Util
import sidev.lib.android.std.tool.util.`fun`.loge
import java.lang.IllegalStateException

@Dao
interface UserFavDao {
    @Query("SELECT * FROM user_fav WHERE username = :username")
    fun getUname(username: String): User?

    @Query("SELECT * FROM user_fav WHERE username LIKE :username")
    fun searchUname(username: String): List<User>

    @Query("SELECT * FROM user_fav")
    fun getAll(): List<User>

    @Query("SELECT * FROM user_fav")
    fun getAllCursor(): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    fun insert(values: ContentValues) = insert(
        User(
            values.getAsString("username"),
            values.getAsString("avatar"),
        )
    )

    @Query("SELECT :cols FROM user_fav")
    fun getAllByCols(cols: String): Cursor

    @Query("SELECT * FROM user_fav WHERE :where")
    fun getAllWhere(where: String): Cursor

    @Query("SELECT * FROM user_fav ORDER BY :by")
    fun getAllOrderBy(by: String): Cursor

    @Query("SELECT :cols FROM user_fav WHERE :where ORDER BY :orderBy")
    fun query(cols: String, where: String, orderBy: String): Cursor

    @Query("SELECT :cols FROM user_fav WHERE :where")
    fun query(cols: String, where: String): Cursor

    fun query(
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val isColsNull = projection == null
        val isWhereNull = selection == null
        val isOrderNull = sortOrder == null

        val _2Nulls = !isColsNull && isWhereNull && isOrderNull
                || !isWhereNull && isColsNull && isOrderNull
                || !isOrderNull && isColsNull && isWhereNull

        loge("query() isColsNull= $isColsNull isWhereNull= $isWhereNull isOrderNull= $isOrderNull _2Nulls= $_2Nulls")

        return when {
            isColsNull && isWhereNull && isOrderNull -> getAllCursor()
            _2Nulls -> when {
                !isColsNull -> getAllByCols(projection!!.joinToString())
                !isWhereNull -> getAllWhere(Util.buildQueryString(selection!!, selectionArgs))
                !isOrderNull -> getAllOrderBy(sortOrder!!)
                else -> throw IllegalStateException("Something wrong happened")
            }
            // 1 null
            else -> when {
                isColsNull -> query("*", Util.buildQueryString(selection!!, selectionArgs), sortOrder!!)
                isWhereNull -> query(projection!!.joinToString(), "TRUE", sortOrder!!)
                isOrderNull -> query(projection!!.joinToString(), Util.buildQueryString(selection!!, selectionArgs),)
                else -> throw IllegalStateException("Something wrong happened")
            }
        }
    }

    @Delete
    fun delete(user: User): Int

    fun delete(username: String): Boolean = getUname(username).let {
        if(it == null) false else delete(it) > 0
    }

    fun isFav(username: String): Boolean = getUname(username) != null
}
package sidev.app.course.dicoding.bab3_modul3.appcommon.model

import android.content.ContentValues
import android.database.Cursor
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.lib.android.std.tool.util.`fun`.loge

/**
 * Data about user that shown in RecyclerView
 */
@Parcelize
@Entity(tableName = "user_fav")
data class User(
    @PrimaryKey val username: String,
    val avatar: String, //remote url
): Parcelable {
    companion object {
        fun fromCursor(c: Cursor): List<User> {
            if(!c.moveToFirst())
                return emptyList()
            val list= mutableListOf<User>()
            do {
                loge("User.from() cols= ${c.columnNames.joinToString()}")
                list += User(c.getString(0), c.getString(1))
            } while(c.moveToNext())
            return list
        }
    }

    fun toContentValues(): ContentValues = ContentValues().apply {
        put(Const.PROP_USERNAME, username)
        put(Const.PROP_AVATAR, avatar)
    }
}
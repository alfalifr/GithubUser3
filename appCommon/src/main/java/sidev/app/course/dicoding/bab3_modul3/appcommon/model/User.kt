package sidev.app.course.dicoding.bab3_modul3.appcommon.model

import android.database.Cursor
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

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
                list += User(c.getString(0), c.getString(1))
            } while(c.moveToNext())
            return list
        }
    }
}
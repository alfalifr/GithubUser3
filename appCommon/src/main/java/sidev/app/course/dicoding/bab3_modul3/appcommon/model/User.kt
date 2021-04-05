package sidev.app.course.dicoding.bab3_modul3.appcommon.model

import android.os.Parcelable
import androidx.room.ColumnInfo
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
): Parcelable
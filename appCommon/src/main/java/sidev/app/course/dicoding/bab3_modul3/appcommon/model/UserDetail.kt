package sidev.app.course.dicoding.bab3_modul3.appcommon.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.User

/**
 * Data about user that shown in detail page
 */
@Parcelize
class UserDetail(
    val user: User,
    val name: String?,
    val company: String?,
    val location: String?,
    val repository: Int,
    val follower: Int,
    val following: Int,
) : Parcelable
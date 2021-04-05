package sidev.app.course.dicoding.bab3_modul3.appcommon.util

import android.os.Bundle
import sidev.app.course.dicoding.bab3_modul3.appcommon.R
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.User

object Const {
    enum class UserListType(val titleRes: Int) {
        //ALL, // For all main user list. Not used
        FOLLOWER(R.string.follower),
        FOLLOWING(R.string.following),
        FAVOURITE(R.string.favourite),
        ;

        /**
         * Creates bundles for fragment to pass.
         */
        fun getArgs(user: User): Bundle = Bundle().apply {
            putString(KEY_OWNER, user.username)
            putInt(KEY_TYPE, ordinal)
        }
    }

    const val DATA = "data"
    private const val URL_USER_SEARCH = "https://api.github.com/search/users?q="
    const val URL_USER_LIST = "https://api.github.com/users"
    const val KEY_OWNER = "owner"
    const val KEY_TYPE = "type"
    const val KEY_USERNAME = "login"
    const val KEY_AVATAR = "avatar_url"
    const val KEY_NAME = "name"
    const val KEY_REPO = "public_repos"
    const val KEY_FOLLOWER = "followers"
    const val KEY_FOLLOWING = "following"
    const val KEY_COMPANY = "company"
    const val KEY_LOCATION = "location"
    const val KEY_ITEMS = "items"
    const val KEY_FAV_APP_INSTALLED = "fav_app_ok"
    const val SHARED_PREF_NAME = "_default_"

    const val PKG_APP_MAIN = "sidev.app.course.dicoding.bab3_modul3.githubuser3"
    const val PKG_APP_COMMON = "sidev.app.course.dicoding.bab3_modul3.appcommon"
    const val PKG_APP_FAV = "sidev.app.course.dicoding.bab3_modul3.githubuser3fav"

    const val ACTION_FAV_LIST = "$PKG_APP_FAV.LIST"
    const val ACTION_USER_DETAIL = "$PKG_APP_COMMON.DETAIL"

    fun getUserListUrl(count: Int)= "$URL_USER_LIST?per_page=$count"
    fun getUserUrl(username: String)= "$URL_USER_LIST/$username"
    fun getUserFollowerListUrl(username: String)= "${getUserUrl(username)}/followers"
    fun getUserFollowingListUrl(username: String)= "${getUserUrl(username)}/following"
    fun getUserSearchUrl(keyword: String)= "$URL_USER_SEARCH$keyword"
}
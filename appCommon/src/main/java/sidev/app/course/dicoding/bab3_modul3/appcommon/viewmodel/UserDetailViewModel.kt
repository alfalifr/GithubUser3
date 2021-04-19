package sidev.app.course.dicoding.bab3_modul3.appcommon.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.runOnUiThread
import org.json.JSONObject
import sidev.app.course.dicoding.bab3_modul3.appcommon.db.UserFavDao
import sidev.app.course.dicoding.bab3_modul3.appcommon.db.UserFavDb
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.User
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.UserDetail
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Util
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Util.Json.getStringOrNull
import sidev.lib.`val`.SuppressLiteral

class UserDetailViewModel(private val ctx: Context): ViewModel() {
    companion object {
        fun getInstance(owner: ViewModelStoreOwner, ctx: Context): UserDetailViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T = UserDetailViewModel(ctx) as T
            }
        ).get(UserDetailViewModel::class.java)
    }

    private val _data = MutableLiveData<UserDetail>()
    private val _favData = MutableLiveData<Boolean>()
    val data: LiveData<UserDetail>
        get()= _data
    val favData: LiveData<Boolean>
        get()= _favData

    private var runningJob: Job?= null

    /**
     * Executed before any async task in `this` runs.
     */
    private var onPreAsyncTask: (() -> Unit)?= null
    fun onPreAsyncTask(f: (() -> Unit)?){
        onPreAsyncTask= f
    }
    private fun doOnPreAsyncTask(){
        onPreAsyncTask?.also { ctx.runOnUiThread { it() } }
    }

    private fun cancelJob(){
        runningJob?.also {
            it.cancel()
            _data.postValue(_data.value)
            runningJob= null
        }
    }

    fun downloadData(uname: String){
        cancelJob()
        doOnPreAsyncTask()
        runningJob = Util.httpGet(ctx, Const.getUserUrl(uname)) { _, content ->
            JSONObject(content).apply {
                val user = UserDetail(
                    User(
                        getString(Const.KEY_USERNAME),
                        getString(Const.KEY_AVATAR),
                    ),
                    getStringOrNull(Const.KEY_NAME),
                    getStringOrNull(Const.KEY_COMPANY),
                    getStringOrNull(Const.KEY_LOCATION),
                    getInt(Const.KEY_REPO),
                    getInt(Const.KEY_FOLLOWER),
                    getInt(Const.KEY_FOLLOWING),
                )
                _data.postValue(user)
            }
        }
    }

    private fun postFavData(block: suspend () -> Boolean) {
        cancelJob()
        doOnPreAsyncTask()
        runningJob = GlobalScope.launch {
            _favData.postValue(block())
        }
    }

    @SuppressLint("Recycle")
    fun isFav(uname: String) = postFavData {
        //Use content provider for simpler code for both module
        ctx.contentResolver.query(Const.UserFavUri.UNAME.completeUri(uname), null, null, null, null)
            ?.let {
                val bool= it.moveToFirst()
                it.close()
                bool
            } == true
    }

    @SuppressLint("Recycle")
    fun deleteFav(uname: String) = postFavData {
        //Use content provider for simpler code for both module
        ctx.contentResolver.delete(
            Const.UserFavUri.UNAME.completeUri(uname), null, null
        ) != 1
        // != 1, because the result boolean shows whether user with `uname` is favorite.
        // In this case, if we successfully deleted user, that user will be NOT favorite.
    }

    @SuppressLint("Recycle")
    fun insertFav(user: User) = postFavData {
        //Use content provider for simpler code for both module
        ctx.contentResolver.insert(Const.UserFavUri.UNAME.completeUri(user.username), user.toContentValues())
        true
    }
}
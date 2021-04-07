package sidev.app.course.dicoding.bab3_modul3.appcommon.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.lifecycle.*
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.runOnUiThread
import org.json.JSONArray
import org.json.JSONObject
import sidev.app.course.dicoding.bab3_modul3.appcommon.db.UserFavDao
import sidev.app.course.dicoding.bab3_modul3.appcommon.db.UserFavDb
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.User
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Util
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.std.tool.util.`fun`.toast
import sidev.lib.collection.asMutableList
import sidev.app.course.dicoding.bab3_modul3.appcommon.R as _R

class UserListViewModel(
    private val ctx: Context,
    private val defaultLoadData: Boolean = true,
    private val dataSource: Const.DataSource = Const.DataSource.ONLINE,
): ViewModel() {

    companion object {
        fun getInstance(
            owner: ViewModelStoreOwner,
            ctx: Context,
            defaultLoadData: Boolean = true,
            dataSource: Const.DataSource = Const.DataSource.ONLINE,
        ): UserListViewModel = ViewModelProvider(
            owner,
            object: ViewModelProvider.Factory {
                @Suppress(SuppressLiteral.UNCHECKED_CAST)
                override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                    UserListViewModel(ctx, defaultLoadData, dataSource) as T
            }
        ).get(UserListViewModel::class.java)
    }

    private val _dataList = MutableLiveData<MutableList<User>>()
    val dataList: LiveData<out List<User>>
        get(){
            val liveData= _dataList
            // Init value
            if(liveData.value == null && defaultLoadData){
                when(dataSource){
                    Const.DataSource.ONLINE -> downloadInitDataList()
                    Const.DataSource.INTERNAL_DB -> queryUserList()
                    Const.DataSource.EXTERNAL_DB -> askUserList()
                }
            }
            return _dataList
        }
    private var runningJob: Job?= null

    private val userFavDao: UserFavDao by lazy {
        UserFavDb.getInstance(ctx).dao()
    }

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
            _dataList.postValue(_dataList.value)
            runningJob= null
        }
    }

    private fun downloadUserList(url: String){
        cancelJob()
        doOnPreAsyncTask()
        runningJob = Util.httpGet(ctx, url, ::checkNetworkException) { _, content ->
            val list= mutableListOf<User>()
            val arr= JSONArray(content.trim())
            for(i in 0 until arr.length()){
                val obj= arr.getJSONObject(i)
                list += User(
                    obj.getString(Const.KEY_USERNAME),
                    obj.getString(Const.KEY_AVATAR),
                )
            }
            _dataList.postValue(list)
        }
    }

    fun downloadInitDataList() = downloadUserList(Const.getUserListUrl(20))
    fun getUserFollower(uname: String) = downloadUserList(Const.getUserFollowerListUrl(uname))
    fun getUserFollowing(uname: String) = downloadUserList(Const.getUserFollowingListUrl(uname))

    fun searchUser(uname: String){
        cancelJob()
        doOnPreAsyncTask()
        runningJob = Util.httpGet(ctx, Const.getUserSearchUrl(uname), ::checkNetworkException) { _, content ->
            JSONObject(content).getJSONArray(Const.KEY_ITEMS).apply {
                val list= mutableListOf<User>()
                for(i in 0 until length()){
                    val obj= getJSONObject(i)
                    list += User(
                        obj.getString(Const.KEY_USERNAME),
                        obj.getString(Const.KEY_AVATAR),
                    )
                }
                _dataList.postValue(list)
            }
        }
    }

    fun queryUserList(){
        cancelJob()
        doOnPreAsyncTask()
        runningJob = GlobalScope.launch {
            _dataList.postValue(userFavDao.getAll().asMutableList())
        }
    }
    fun queryUser(uname: String){
        cancelJob()
        doOnPreAsyncTask()
        runningJob = GlobalScope.launch {
            _dataList.postValue(userFavDao.searchUname(uname).asMutableList())
        }
    }

    @SuppressLint("Recycle")
    fun askUserList(){
        cancelJob()
        doOnPreAsyncTask()
        runningJob = GlobalScope.launch {
            val client = ctx.contentResolver.acquireContentProviderClient(
                Const.UserFavUri.ALL.completeUri()
            )
            client?.query(
                Const.UserFavUri.ALL.completeUri(), null, null, null, null
            )?.also {
                _dataList.postValue(
                    User.fromCursor(it).asMutableList()
                )
                it.close()
            } ?: run {
                ctx.runOnUiThread { ctx.toast(ctx.getString(_R.string.cant_access_user_data)) }
            }
            if(Build.VERSION.SDK_INT < 24)
                client?.release()
            else
                client?.close()
        }
    }

    @SuppressLint("Recycle")
    fun askUser(uname: String){
        cancelJob()
        doOnPreAsyncTask()
        runningJob = GlobalScope.launch {
            val client = ctx.contentResolver.acquireContentProviderClient(
                Const.UserFavUri.LIKE_UNAME.completeUri(uname)
            )
            client?.query(
                Const.UserFavUri.LIKE_UNAME.completeUri(uname), null, null, null, null
            )?.also {
                _dataList.postValue(
                    User.fromCursor(it).asMutableList()
                )
                it.close()
            } ?: run {
                ctx.runOnUiThread { ctx.toast(ctx.getString(_R.string.cant_access_user_data)) }
            }
            if(Build.VERSION.SDK_INT < 24)
                client?.release()
            else
                client?.close()
        }
    }

    private fun checkNetworkException(code: Int, e: VolleyError){
        if(code == 404){
            _dataList.postValue(mutableListOf())
        } else {
            loge("NETWORK ERROR!!!", e)
            if(e is TimeoutError){
                ctx.toast(ctx.getString(_R.string.toast_connection_timeout))
            } else {
                throw IllegalStateException("Something wrong happened is connection", e)
            }
        }
    }
}
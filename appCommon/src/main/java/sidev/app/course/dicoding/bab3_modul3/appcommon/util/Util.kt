package sidev.app.course.dicoding.bab3_modul3.appcommon.util

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast
import org.json.JSONObject
import sidev.app.course.dicoding.bab3_modul3.appcommon.R
import sidev.lib.`val`.SuppressLiteral
import sidev.lib.android.std.tool.util._NetworkUtil
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.structure.data.value.varOf
import java.util.*

object Util {
    object Json {
        fun JSONObject.getStringOrNull(key: String): String? = getString(key).let {
            if(it != "null") it else null
        }
    }

    fun setDrawableTint(tv: TextView, @ColorRes colorRes: Int = R.color.colorPrimaryDark){
        for(dr in tv.compoundDrawables){
            if(dr != null)
                _ViewUtil.setColorTintRes(dr, colorRes)
        }
    }

    fun httpGet(
        c: Context,
        url: String,
        onError: ((code: Int, e: VolleyError) -> Unit)?= null,
        onResponse: (code: Int, content: String) -> Unit,
    ) : Job = GlobalScope.launch(Dispatchers.IO) {
        if(_NetworkUtil.isNetworkActive(c)){
            Volley.newRequestQueue(c).add(
                createVolleyRequest(
                Request.Method.GET, url, onError, onResponse
            )
            )
        } else {
            c.runOnUiThread {
                toast(c.getString(R.string.toast_check_connection))
            }
        }
    }

    fun createVolleyRequest(
        method: Int,
        url: String,
        onError: ((code: Int, e: VolleyError) -> Unit)?= null,
        onResponse: (code: Int, content: String) -> Unit,
    ): StringRequest {
        val code= varOf(-1)
        return object: StringRequest(
            method,
            url,
            Response.Listener { onResponse(code.value, it) },
            Response.ErrorListener { onError?.invoke(it.networkResponse?.statusCode ?: -1, it) }
        ) {
            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                response?.also {
                    code.value= it.statusCode
                    if(it.statusCode.toString().startsWith("4")){
                        return Response.success(
                            response.headers?.get("Content-Length"),
                            HttpHeaderParser.parseCacheHeaders(response)
                        )
                    }
                }
                return super.parseNetworkResponse(response)
            }
        }
    }

    fun getSharedPref(ctx: Context, mode: Int = Context.MODE_PRIVATE): SharedPreferences =
        ctx.getSharedPreferences(Const.SHARED_PREF_NAME, mode)

    fun buildQueryString(rawString: String, args: Array<out String>?): String {
        if(args == null)
            return rawString
        var res= rawString
        var i= -1
        var argI= 0
        while(++i < res.length){
            if(res[i] == '?'){
                val arg= args[argI++]
                res= res.substring(0, i) +arg +res.substring(i+1)
                i += arg.length -1
            }
        }
        return res
    }


    fun setAlarm(
        c: Context, pi: PendingIntent,
        hour: Int = 1, minute: Int = 0, second: Int = 0,
        repeat: Boolean = true
    ){
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        cal.set(Calendar.SECOND, second)
        val manager = c.getSystemService<AlarmManager>()!!

        if(repeat)
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.timeInMillis, AlarmManager.INTERVAL_DAY, pi)
        else
            manager.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
    }

    fun stopAlarm(c: Context, pi: PendingIntent){
        pi.cancel()
        c.getSystemService<AlarmManager>()!!.cancel(pi)
    }

    @Suppress(SuppressLiteral.NAME_SHADOWING)
    fun showNotif(
        c: Context,
        smIcon: Int = R.drawable.app_logo_mini,
        title: String = c.getString(R.string.template_title),
        desc: String = c.getString(R.string.template_text),
        channelId: String = "CHANNEL_ID",
        channelName: String = "CHANNEL_NAME",
        channelDesc: String = "CHANNEL_DESC",
        notifId: Int = 1,
        pendingIntent: PendingIntent?= null,
    ){
        val manager = c.getSystemService<NotificationManager>()!!

        val builder = NotificationCompat.Builder(c, channelId)
            .setSmallIcon(smIcon)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setContentTitle(title)
            .setContentText(desc)
            .setAutoCancel(true)

        if(pendingIntent != null)
            builder.setContentIntent(pendingIntent)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = channelDesc

            manager.createNotificationChannel(channel)
        }

        val notif = builder.build()
        manager.notify(notifId, notif)
    }
}
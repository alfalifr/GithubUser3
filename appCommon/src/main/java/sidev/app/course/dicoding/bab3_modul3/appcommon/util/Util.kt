package sidev.app.course.dicoding.bab3_modul3.appcommon.util

import android.content.Context
import android.content.SharedPreferences
import android.widget.TextView
import androidx.annotation.ColorRes
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
import sidev.lib.android.std.tool.util._NetworkUtil
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.structure.data.value.varOf

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
}
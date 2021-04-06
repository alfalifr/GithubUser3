package sidev.app.course.dicoding.bab3_modul3.appcommon.act

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import sidev.app.course.dicoding.bab3_modul3.appcommon.R
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Util
import sidev.lib.android.std.tool.util._ThreadUtil
import sidev.lib.android.std.tool.util.`fun`.startAct

class SplashAct: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_splash)

        _ThreadUtil.delayRun(3000){
            startAct<UserListAct>()
            finish()
        }

        Util.getSharedPref(this).edit {
            putBoolean(Const.KEY_FAV_APP_INSTALLED, checkFavAppPresence())
        }
    }

    private fun checkFavAppPresence(): Boolean = try {
        packageManager.getPackageInfo(Const.PKG_APP_FAV, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException){
        false
    }
}
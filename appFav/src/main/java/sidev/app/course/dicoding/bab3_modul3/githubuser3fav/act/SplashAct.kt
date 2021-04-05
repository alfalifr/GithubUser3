package sidev.app.course.dicoding.bab3_modul3.githubuser3fav.act

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sidev.app.course.dicoding.bab3_modul3.githubuser3fav.R
import sidev.lib.android.std.tool.util._ThreadUtil
import sidev.lib.android.std.tool.util.`fun`.loge
import sidev.lib.android.std.tool.util.`fun`.startAct

class SplashAct: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_splash)

        loge("packageName= $packageName")

        _ThreadUtil.delayRun(3000){
            startAct<UserFavListAct>()
            finish()
        }
    }
}
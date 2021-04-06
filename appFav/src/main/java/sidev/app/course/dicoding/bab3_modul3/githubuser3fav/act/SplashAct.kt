package sidev.app.course.dicoding.bab3_modul3.githubuser3fav.act

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.app.course.dicoding.bab3_modul3.githubuser3fav.R
import sidev.lib.android.std.tool.util._ThreadUtil
import sidev.lib.android.std.tool.util.`fun`.startAct

class SplashAct: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.NoActBar)
        setContentView(R.layout.page_splash)

        if(intent.action == Const.ACTION_DIRECT){
            startAct<UserFavListAct>()
            finish()
        } else {
            _ThreadUtil.delayRun(3000){
                startAct<UserFavListAct>()
                finish()
            }
        }
    }
}
package sidev.app.course.dicoding.bab3_modul3.appcommon.act

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sidev.app.course.dicoding.bab3_modul3.appcommon.R
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
    }
}
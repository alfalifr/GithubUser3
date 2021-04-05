package sidev.app.course.dicoding.bab3_modul3.appcommon.act

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import sidev.app.course.dicoding.bab3_modul3.appcommon.R
import sidev.app.course.dicoding.bab3_modul3.appcommon.frag.UserListFrag
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Util
import sidev.lib.android.std.tool.util.`fun`.commitFrag

class UserListAct: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_single_frag)
        commitFrag(UserListFrag(), fragContainerId = R.id.fragment, forceReplace = false)
        setTitle(R.string.user_list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val isFavAppInstalled = Util.getSharedPref(this)
            .getBoolean(Const.KEY_FAV_APP_INSTALLED, false)
        menuInflater.inflate(if(isFavAppInstalled) R.menu.main_page_fav else R.menu.main_page, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.set_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.btn_fav -> startActivity(
                Intent(Const.ACTION_FAV_LIST)
            )
        }
        return super.onOptionsItemSelected(item)
    }
}
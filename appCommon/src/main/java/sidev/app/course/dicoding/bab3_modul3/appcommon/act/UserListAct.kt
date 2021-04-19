package sidev.app.course.dicoding.bab3_modul3.appcommon.act

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import sidev.app.course.dicoding.bab3_modul3.appcommon.R
import sidev.app.course.dicoding.bab3_modul3.appcommon.frag.UserListFrag
import sidev.lib.android.std.tool.util.`fun`.commitFrag
import sidev.lib.android.std.tool.util.`fun`.startAct
import java.lang.IllegalStateException

class UserListAct: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_single_frag)
        commitFrag(UserListFrag(), fragContainerId = R.id.fragment, forceReplace = false)
        setTitle(R.string.user_list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_page_fav, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.set_language -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            R.id.fav_page -> startAct<UserFavListAct>()
            R.id.set_other -> startAct<SettingsAct>()
            else -> throw IllegalStateException("There is an illegal itemId (${item.itemId}) in menu")
        }
        return super.onOptionsItemSelected(item)
    }
}
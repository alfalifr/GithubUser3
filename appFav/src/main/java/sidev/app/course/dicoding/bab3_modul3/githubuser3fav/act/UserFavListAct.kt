package sidev.app.course.dicoding.bab3_modul3.githubuser3fav.act

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sidev.app.course.dicoding.bab3_modul3.appcommon.R as _R
import sidev.app.course.dicoding.bab3_modul3.appcommon.frag.UserListFrag
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.app.course.dicoding.bab3_modul3.githubuser3fav.R
import sidev.lib.android.std.tool.util.`fun`.commitFrag

class UserFavListAct: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_R.layout.page_single_frag)
        commitFrag(
            UserListFrag().apply {
                arguments = Bundle().apply {
                    putInt(Const.KEY_SOURCE, Const.UserListType.FAVOURITE.ordinal)
                }
            },
            fragContainerId = _R.id.fragment, forceReplace = false
        )
        setTitle(R.string.favourite_list)
    }
}
package sidev.app.course.dicoding.bab3_modul3.appcommon.act

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import sidev.app.course.dicoding.bab3_modul3.appcommon.R
import sidev.app.course.dicoding.bab3_modul3.appcommon.frag.UserListFrag
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.lib.android.std.tool.util.`fun`.commitFrag

class UserFavListAct: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_single_frag)
        commitFrag(
            UserListFrag().apply {
                arguments = Bundle().apply {
                    putInt(Const.KEY_SOURCE, Const.UserListType.FAVOURITE.ordinal)
                    putBoolean(Const.DATA_LOAD_ON_RESUME, true)
                }
            },
            fragContainerId = R.id.fragment, forceReplace = false
        )
        setTitle(R.string.favourite_list)
    }
}
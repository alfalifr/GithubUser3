package sidev.app.course.dicoding.bab3_modul3.appcommon.adp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import sidev.app.course.dicoding.bab3_modul3.appcommon.frag.UserListFrag
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.User
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const

class UserDetailVpAdp(act: FragmentActivity, private val user: User): FragmentStateAdapter(act) {
    override fun getItemCount(): Int = Const.UserListType.values().size

    override fun createFragment(position: Int): Fragment = UserListFrag::class.java.newInstance().apply {
        arguments= Const.UserListType.values()[position].getArgs(user)
    }
}
package sidev.app.course.dicoding.bab3_modul3.appcommon.act

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import org.jetbrains.anko.imageResource
import sidev.app.course.dicoding.bab3_modul3.appcommon.R
import sidev.app.course.dicoding.bab3_modul3.appcommon.adp.UserDetailVpAdp
import sidev.app.course.dicoding.bab3_modul3.appcommon.databinding.PageDetailBinding
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.User
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Util
import sidev.app.course.dicoding.bab3_modul3.appcommon.viewmodel.UserDetailViewModel
import sidev.lib.android.std.tool.util._ViewUtil
import sidev.lib.android.std.tool.util.`fun`.loge

class UserDetailAct: AppCompatActivity() {
    private lateinit var data: User
    private lateinit var binding: PageDetailBinding
    private val vm: UserDetailViewModel by lazy {
        UserDetailViewModel.getInstance(this, this)
    }
    private val vpAdp: UserDetailVpAdp by lazy {
        UserDetailVpAdp(this, data)
    }
    private val vpTitles by lazy {
        Const.UserListType.values().map { it.titleRes }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= PageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle(R.string.user_detail)

        // If the extra is String. This is because we can't pass parcelable extra via fillIntent from widget.
        data= intent.getParcelableExtra(Const.DATA) ?: run {
            val username = intent.getStringExtra(Const.KEY_USERNAME)!! // It's obvious to use !! since `data` should not be null.
            User(username, "")
        }

        initFavBtn()

        binding.apply {
            val icPadding= _ViewUtil.dpToPx(7f, this@UserDetailAct).toInt()

            tvLocation.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_location_, 0, 0, 0)
            tvCompany.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_company, 0, 0, 0)
            tvFollower.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_follower, 0, 0, 0)
            tvRepository.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_repo, 0, 0, 0)

            tvCompany.compoundDrawablePadding = icPadding
            tvLocation.compoundDrawablePadding = icPadding
            tvFollower.compoundDrawablePadding = icPadding
            tvRepository.compoundDrawablePadding = icPadding

            val value = TypedValue()
            theme.resolveAttribute(R.attr.colorOnPrimary, value, true)
            val colorRes= value.data

            Util.setDrawableTint(tvCompany, colorRes)
            Util.setDrawableTint(tvLocation, colorRes)
            Util.setDrawableTint(tvFollower, colorRes)
            Util.setDrawableTint(tvRepository, colorRes)

            vp.adapter = vpAdp
            TabLayoutMediator(tabs, vp) { tab, pos ->
                tab.text = getString(vpTitles[pos])
            }.attach()
        }

        vm.apply {
            data.observe(this@UserDetailAct){
                if(it != null){
                    binding.apply {
                        tvUname.text= it.user.username
                        Glide.with(this@UserDetailAct)
                            .load(it.user.avatar)
                            .into(ivProfile)
                        tvName.textOrNull(it.name)
                        tvCompany.textOrNull(it.company)
                        tvLocation.textOrNull(it.location)
                        tvFollower.text= resources.getQuantityString(R.plurals.followers, it.follower, it.follower)
                        tvFollowing.text= resources.getQuantityString(R.plurals.followings, it.following, it.following)
                        tvRepository.text= resources.getQuantityString(R.plurals.repositories, it.repository, it.repository)
                    }
                    showLoading(false)
                }
            }
            onPreAsyncTask {
                showLoading()
            }
            downloadData(this@UserDetailAct.data.username)
        }
    }

    private fun showLoading(show: Boolean = true){
        binding.apply {
            if(show){
                vgContent.visibility= View.GONE
                pb.visibility= View.VISIBLE
            } else {
                vgContent.visibility= View.VISIBLE
                pb.visibility= View.GONE
            }
        }
    }

    private fun initFavBtn(){
        // init other config of binding.btnFav only if `isFavAppInstalled` for efficiency
        binding.btnFav.apply {
            visibility = View.VISIBLE
            vm.favData.observe(this@UserDetailAct) {
                loge("isFav() = $it")
                if(it != null){
                    imageResource = if(it) R.drawable.ic_heart else R.drawable.ic_heart_hollow
                }
            }
            vm.isFav(data.username)
            setOnClickListener {
                val isFav = vm.favData.value!! // !! 'cause we assume that the value is already loaded from db 'cuase it shouldn't take long.
                if(isFav){
                    vm.deleteFav(data.username)
                } else {
                    vm.insertFav(data)
                }
            }
        }
    }

    /**
     * If [txt] is null, then `this` will be [View.GONE].
     */
    private fun TextView.textOrNull(txt: String?){
        text= txt
        visibility= if(txt != null) View.VISIBLE else View.GONE
    }
}
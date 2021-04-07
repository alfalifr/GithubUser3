package sidev.app.course.dicoding.bab3_modul3.appcommon.act

import android.app.PendingIntent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import sidev.app.course.dicoding.bab3_modul3.appcommon.R
import sidev.app.course.dicoding.bab3_modul3.appcommon.receiver.AlarmNotifReceiver
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Util

class SettingsAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_settings)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setTitle(R.string.setting)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        private lateinit var switch: SwitchPreferenceCompat

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            switch = findPreference("notif")!!
            switch.setOnPreferenceChangeListener { _, newValue ->
                AlarmNotifReceiver.setOn(requireContext(), newValue == true)
                true
            }
            initConfig()
        }

        private fun initConfig(){
            val firstRun = Util.getSharedPref(requireContext())
                .getBoolean(Const.KEY_FIRST_RUN, true)
            if(firstRun){
                AlarmNotifReceiver.setOn(requireContext())
                switch.isChecked = true
                Util.getSharedPref(requireContext()).edit {
                    putBoolean(Const.KEY_FIRST_RUN, false)
                }
            } else {
                // If user installs update, but previously the preference was on, then just setOn the alarm.
                if(switch.isChecked
                    && AlarmNotifReceiver.getAlarmPendingIntent(requireContext(), PendingIntent.FLAG_NO_CREATE) == null
                ){
                    AlarmNotifReceiver.setOn(requireContext())
                }
            }
        }
    }
}
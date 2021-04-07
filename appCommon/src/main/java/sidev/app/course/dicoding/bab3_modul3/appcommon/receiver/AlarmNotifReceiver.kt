package sidev.app.course.dicoding.bab3_modul3.appcommon.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import sidev.app.course.dicoding.bab3_modul3.appcommon.R
import sidev.app.course.dicoding.bab3_modul3.appcommon.act.SplashAct
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Util
import sidev.lib.android.std.tool.util.`fun`.loge

class AlarmNotifReceiver: BroadcastReceiver(){
    companion object {
        fun getAlarmPendingIntent(c: Context, flags: Int = PendingIntent.FLAG_UPDATE_CURRENT): PendingIntent? {
            val intent = Intent(c, AlarmNotifReceiver::class.java)
            intent.action = Const.ACTION_ALARM_NOTIF
            return PendingIntent.getBroadcast(c, 0, intent, flags)
        }
        fun setOn(c: Context, on: Boolean = true){
            if(on){
                Util.setAlarm(
                    c, getAlarmPendingIntent(c)!!, // It's safe to use !! here
                    9, // Set the time for the notification here
                )
            } else {
                val pi = getAlarmPendingIntent(c, PendingIntent.FLAG_NO_CREATE)
                    ?: run {
                        // This could happen when user install update,
                        // so the pendingIntent got erased while the preference still persists in storage.
                        // The resulting pendingIntent will null, so just skip the remaining process.
                        loge("Previous pendingIntent got erased, but the preference still persists in storage, then just return")
                        return
                    }
                Util.stopAlarm(c, pi)
            }
        }
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context != null){
            if(intent?.action == Const.ACTION_ALARM_NOTIF){
                Util.showNotif(
                    context,
                    title = context.getString(R.string.hello),
                    desc = context.getString(R.string.notif_desc),
                    pendingIntent = PendingIntent.getActivity(
                        context, 0,
                        Intent(context, SplashAct::class.java),
                        PendingIntent.FLAG_ONE_SHOT
                    )
                )
                //context.toast("Alarm lewat bro")
            }
        }
    }
}
package sidev.app.course.dicoding.bab3_modul3.githubuser3fav.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import org.jetbrains.anko.toast
import sidev.app.course.dicoding.bab3_modul3.appcommon.act.UserDetailAct
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.app.course.dicoding.bab3_modul3.githubuser3fav.R
import sidev.app.course.dicoding.bab3_modul3.githubuser3fav.service.WidgetFavService
import sidev.app.course.dicoding.bab3_modul3.githubuser3fav.util.ConstFav
import sidev.lib.android.std.tool.util.`fun`.startAct
import java.lang.IllegalStateException

class FavStackWidget: AppWidgetProvider() {
    /**
     * Called in response to the [AppWidgetManager.ACTION_APPWIDGET_UPDATE] and
     * [AppWidgetManager.ACTION_APPWIDGET_RESTORED] broadcasts when this AppWidget
     * provider is being asked to provide [RemoteViews][android.widget.RemoteViews]
     * for a set of AppWidgets.  Override this method to implement your own AppWidget functionality.
     *
     * {@more}
     *
     * @param context   The [Context][android.content.Context] in which this receiver is
     * running.
     * @param appWidgetManager A [AppWidgetManager] object you can call [                  ][AppWidgetManager.updateAppWidget] on.
     * @param appWidgetIds The appWidgetIds for which an update is needed.  Note that this
     * may be all of the AppWidget instances for this provider, or just
     * a subset of them.
     *
     * @see AppWidgetManager.ACTION_APPWIDGET_UPDATE
     */
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for(id in appWidgetIds)
            updateAppWidget(context, appWidgetManager, id)
    }

    /**
     * Updates widget individually.
     */
    private fun updateAppWidget(ctx: Context, manager: AppWidgetManager, widgetId: Int){
        val serviceIntent = Intent(ctx, WidgetFavService::class.java)
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        serviceIntent.data = serviceIntent.toUri(Intent.URI_INTENT_SCHEME).toUri()

        val itemIntent = Intent(ctx, FavStackWidget::class.java)
        itemIntent.action = ConstFav.ACTION_TOAST
        itemIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        val itemPendingIntent = PendingIntent.getBroadcast(ctx, 0, itemIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val refreshIntent = Intent(ctx, FavStackWidget::class.java)
        refreshIntent.action = ConstFav.ACTION_REFRESH
        refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        val refreshPendingIntent = PendingIntent.getBroadcast(ctx, widgetId, refreshIntent, 0)

        val views= RemoteViews(ctx.packageName, R.layout.widget_fav)
        views.setEmptyView(R.id.sv, R.id.tv_no_data)
        views.setRemoteAdapter(R.id.sv, serviceIntent)
        views.setPendingIntentTemplate(R.id.sv, itemPendingIntent)
        views.setOnClickPendingIntent(R.id.iv_refresh, refreshPendingIntent)

        manager.updateAppWidget(widgetId, views)
    }

    /**
     * Implements [BroadcastReceiver.onReceive] to dispatch calls to the various
     * other methods on AppWidgetProvider.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        when(intent?.action){
            ConstFav.ACTION_TOAST -> {
                val username = intent.getStringExtra(Const.KEY_USERNAME)
                    ?: throw IllegalStateException("Extra with key `Const.KEY_USERNAME` must not be null")
                context.toast("username: $username")
                context.startAct<UserDetailAct>(Const.KEY_USERNAME to username){
                    it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
            ConstFav.ACTION_REFRESH -> {
                val id= intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 0)
                context.toast(context.getString(R.string.refreshing))
                updateAppWidget(context, AppWidgetManager.getInstance(context), id)
            }
        }
    }
}
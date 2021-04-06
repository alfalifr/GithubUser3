package sidev.app.course.dicoding.bab3_modul3.githubuser3fav.adp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import org.jetbrains.anko.runOnUiThread
import sidev.app.course.dicoding.bab3_modul3.appcommon.model.User
import sidev.app.course.dicoding.bab3_modul3.appcommon.util.Const
import sidev.app.course.dicoding.bab3_modul3.githubuser3fav.R
import sidev.app.course.dicoding.bab3_modul3.appcommon.R as _R
import sidev.lib.android.std.tool.util.`fun`.toast
import java.net.HttpURLConnection
import java.net.URL

class WidgetFavAdp(private val ctx: Context): RemoteViewsService.RemoteViewsFactory {
    private var dataList: List<User>? = null
    /**
     * Called when your factory is first constructed. The same factory may be shared across
     * multiple RemoteViewAdapters depending on the intent passed.
     */
    override fun onCreate() {}

    /**
     * Called when notifyDataSetChanged() is triggered on the remote adapter. This allows a
     * RemoteViewsFactory to respond to data changes by updating any internal references.
     *
     * Note: expensive tasks can be safely performed synchronously within this method. In the
     * interim, the old data will be displayed within the widget.
     *
     * @see android.appwidget.AppWidgetManager.notifyAppWidgetViewDataChanged
     */
    @SuppressLint("Recycle")
    override fun onDataSetChanged() {
        ctx.contentResolver.query(
            Const.UserFavUri.ALL.completeUri(), null, null, null, null,
        )?.also {
            dataList = User.fromCursor(it)
            it.close()
        } ?: run {
            ctx.runOnUiThread { ctx.toast(ctx.getString(_R.string.cant_access_user_data)) }
        }
    }

    /**
     * Called when the last RemoteViewsAdapter that is associated with this factory is
     * unbound.
     */
    override fun onDestroy() {}

    /**
     * See [Adapter.getCount]
     *
     * @return Count of items.
     */
    override fun getCount(): Int = dataList?.size ?: 0

    /**
     * See [Adapter.getView].
     *
     * Note: expensive tasks can be safely performed synchronously within this method, and a
     * loading view will be displayed in the interim. See [.getLoadingView].
     *
     * @param position The position of the item within the Factory's data set of the item whose
     * view we want.
     * @return A RemoteViews object corresponding to the data at the specified position.
     */
    override fun getViewAt(position: Int): RemoteViews = RemoteViews(ctx.packageName, R.layout.item_fav).apply {
        val data = dataList!![position] //It's obvious that this method is only called when `dataList` is not null and not empty.
        val conn = URL(data.avatar).openConnection() as HttpURLConnection
        if(conn.responseCode == 200){
            val bm = BitmapFactory.decodeStream(conn.inputStream)
            setImageViewBitmap(R.id.iv, bm)
        } else {
            setImageViewResource(R.id.iv, R.drawable.ic_error)
        }

        setTextViewText(R.id.tv, data.username)

        val fillIntent = Intent()
        fillIntent.putExtra(Const.KEY_USERNAME, data.username)
        setOnClickFillInIntent(R.id.root, fillIntent)
    }

    /**
     * This allows for the use of a custom loading view which appears between the time that
     * [.getViewAt] is called and returns. If null is returned, a default loading
     * view will be used.
     *
     * @return The RemoteViews representing the desired loading view.
     */
    override fun getLoadingView(): RemoteViews = RemoteViews(ctx.packageName, R.layout.item_loading)

    /**
     * See [Adapter.getViewTypeCount].
     *
     * @return The number of types of Views that will be returned by this factory.
     */
    override fun getViewTypeCount(): Int = 1

    /**
     * See [Adapter.getItemId].
     *
     * @param position The position of the item within the data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    override fun getItemId(position: Int): Long = 0

    /**
     * See [Adapter.hasStableIds].
     *
     * @return True if the same id always refers to the same object.
     */
    override fun hasStableIds(): Boolean = true
}
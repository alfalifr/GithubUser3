package sidev.app.course.dicoding.bab3_modul3.githubuser3fav.service

import android.content.Intent
import android.widget.RemoteViewsService
import sidev.app.course.dicoding.bab3_modul3.githubuser3fav.adp.WidgetFavAdp

class WidgetFavService: RemoteViewsService() {
    /**
     * To be implemented by the derived service to generate appropriate factories for
     * the data.
     */
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory = WidgetFavAdp(applicationContext)
}
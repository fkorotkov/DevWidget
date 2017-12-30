package com.tasomaniac.devdrawer.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.data.Dao
import com.tasomaniac.devdrawer.data.Widget
import com.tasomaniac.devdrawer.data.deleteWidgets
import com.tasomaniac.devdrawer.rx.SchedulingStrategy
import dagger.android.AndroidInjection
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import javax.inject.Inject

class WidgetProvider : AppWidgetProvider() {

  @Inject lateinit var dao: Dao
  @Inject lateinit var scheduling: SchedulingStrategy
  @Inject lateinit var appWidgetManager: AppWidgetManager

  private var disposable: Disposable = Disposables.empty()

  override fun onReceive(context: Context, intent: Intent) {
    AndroidInjection.inject(this, context)
    super.onReceive(context, intent)
  }

  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
    if (appWidgetIds.isEmpty()) return

    disposable.dispose()
    disposable = dao.findWidgetById(*appWidgetIds)
        .compose(scheduling.forFlowable())
        .subscribe {
          it.updateWith(context)
        }
  }

  override fun onDeleted(context: Context, appWidgetIds: IntArray) {
    val widgets = appWidgetIds.map { Widget(it) }
    dao.deleteWidgets(*widgets.toTypedArray())
        .compose(scheduling.forCompletable())
        .subscribe()
  }

  private fun Widget.updateWith(context: Context) {
    val remoteViews = RemoteViews(context.packageName, R.layout.app_widget)

    remoteViews.setTextViewText(R.id.widgetTitle, name)
    remoteViews.setRemoteAdapter(R.id.widgetAppList, remoteAdapter(context, appWidgetId))

    appWidgetManager.updateAppWidget(appWidgetId, remoteViews)
  }

  private fun remoteAdapter(context: Context, appWidgetId: Int): Intent {
    return Intent(context, WidgetViewsService::class.java).apply {
      putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
      data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
    }
  }
}

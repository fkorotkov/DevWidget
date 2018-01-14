package com.tasomaniac.devdrawer.widget

import android.app.Application
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import com.tasomaniac.devdrawer.R
import com.tasomaniac.devdrawer.settings.NightMode
import com.tasomaniac.devdrawer.settings.NightMode.OFF
import com.tasomaniac.devdrawer.settings.NightMode.ON
import com.tasomaniac.devdrawer.settings.NightModePreferences
import javax.inject.Inject

class WidgetResources @Inject constructor(
    private val nightModePreferences: NightModePreferences,
    context: Application
) {

  val deleteIcon
    @DrawableRes get() = when (nightModePreferences.mode) {
      NightMode.OFF -> R.drawable.ic_delete
      NightMode.ON -> R.drawable.ic_delete_light
    }

  val settingsIcon
    @DrawableRes get() = when (nightModePreferences.mode) {
      NightMode.OFF -> R.drawable.ic_settings
      NightMode.ON -> R.drawable.ic_settings_light
    }

  private val foregroundDark = ContextCompat.getColor(context, R.color.foregroundDark)
  private val foregroundLight = ContextCompat.getColor(context, R.color.foregroundLight)

  val foregroundColor
    @ColorInt get() = when (nightModePreferences.mode) {
      OFF -> foregroundDark
      ON -> foregroundLight
    }

  val foregroundColorInverse
    @ColorInt get() = when (nightModePreferences.mode) {
      OFF -> foregroundLight
      ON -> foregroundDark
    }

}

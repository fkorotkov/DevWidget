package com.tasomaniac.devwidget.settings

import android.content.Context
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

  @Inject lateinit var settings: @JvmSuppressWildcards Set<Settings>

  override fun onAttach(context: Context) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onCreatePreferences(bundle: Bundle?, s: String?) {
    settings.forEach { it.setup() }
  }

  override fun onResume() {
    super.onResume()
    settings.forEach { it.resume() }
  }

  override fun onPause() {
    settings.forEach { it.pause() }
    super.onPause()
  }

  override fun onDestroy() {
    settings.forEach { it.release() }
    super.onDestroy()
  }

  companion object {
    fun newInstance() = SettingsFragment()
  }
}
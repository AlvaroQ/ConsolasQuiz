package com.quiz.videoconsolas.ui.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.quiz.videoconsolas.BuildConfig
import com.quiz.videoconsolas.R
import com.quiz.videoconsolas.common.startActivity
import com.quiz.videoconsolas.ui.moreApps.MoreAppsActivity
import com.quiz.videoconsolas.utils.Constants
import com.quiz.videoconsolas.utils.rateApp
import com.quiz.videoconsolas.utils.shareApp

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // rate_app
        val rateApp: Preference? = findPreference("rate_app")
        rateApp?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            rateApp(requireContext())
            false
        }

        // share
        val share: Preference? = findPreference("share")
        share?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            shareApp(-1, requireContext())
            false
        }

        // Version
        val version: Preference? = findPreference("version")
        version?.summary = "${getString(R.string.settings_version)} ${BuildConfig.VERSION_NAME} (Build ${BuildConfig.VERSION_CODE})"

        // more_apps
        val moreApps: Preference? = findPreference("more_apps")
        moreApps?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_STORE)))
            } catch (_: ActivityNotFoundException) { }
            false
        }
    }
}
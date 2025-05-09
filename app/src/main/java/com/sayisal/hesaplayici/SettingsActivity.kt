package com.sayisal.hesaplayici

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val historyPreference: Preference? = findPreference("history")
            historyPreference?.setOnPreferenceClickListener {
                val intent = Intent(context, HistoryActivity::class.java)
                startActivity(intent)
                true
            }

            // Geri bildirim öğesi (EKLEME)
            findPreference<Preference>("feedback")?.setOnPreferenceClickListener {
                val appPackageName = requireContext().packageName
                try {
                    // Önce Play Store uygulamasını dene
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                } catch (e: ActivityNotFoundException) {
                    try {
                        // Play Store yoksa web tarayıcıyla aç
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                    } catch (e: Exception) {
                        // Hiçbiri çalışmazsa hata mesajı göster
                        Toast.makeText(requireContext(), "Play Store açılamadı", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }

            // Koyu tema tercihini dinleme ve uygulama
            val darkModePreference: SwitchPreferenceCompat? = findPreference("dark_mode")

            darkModePreference?.setOnPreferenceChangeListener { _, newValue ->
                if (newValue as Boolean) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                true
            }
        }
    }
}
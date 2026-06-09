package com.example.chambape.data.repository

import android.content.Context

class SettingsPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var pushNotifications: Boolean
        get() = prefs.getBoolean("push_notifications", true)
        set(v) { prefs.edit().putBoolean("push_notifications", v).apply() }

    var emailNotifications: Boolean
        get() = prefs.getBoolean("email_notifications", false)
        set(v) { prefs.edit().putBoolean("email_notifications", v).apply() }

    var nearbyShifts: Boolean
        get() = prefs.getBoolean("nearby_shifts", true)
        set(v) { prefs.edit().putBoolean("nearby_shifts", v).apply() }

    var darkMode: Boolean
        get() = prefs.getBoolean("dark_mode", false)
        set(v) { prefs.edit().putBoolean("dark_mode", v).apply() }

    var language: String
        get() = prefs.getString("language", "Español") ?: "Español"
        set(v) { prefs.edit().putString("language", v).apply() }
}

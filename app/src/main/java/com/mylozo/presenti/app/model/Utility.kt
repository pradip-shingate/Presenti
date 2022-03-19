package com.mylozo.presenti.app.model

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.*

class Utility {
    companion object {
        fun loadLocale(activity:Activity) {
            val locale = Locale(EmployeePrefs.getLanguage(activity))
            Locale.setDefault(locale)
            val resources: Resources = activity.resources
            val config: Configuration = resources.configuration
            config.setLocale(locale)

            activity.resources.updateConfiguration(
                config,
                activity.resources.displayMetrics
            )
        }
    }
}
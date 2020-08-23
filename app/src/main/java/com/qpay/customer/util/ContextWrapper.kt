package com.qpay.customer.util

import android.content.Context
import android.os.Build
import android.os.LocaleList
import java.util.*

/**
 * Created by Priyanka on 17/4/19.
 */
class ContextWrapper(base: Context) : android.content.ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, newLocale: Locale): ContextWrapper {
            val res = context.resources
            val configuration = res.configuration
            when {
                Build.VERSION.SDK_INT > Build.VERSION_CODES.N -> {
                    configuration.setLocale(newLocale)

                    val localeList = LocaleList(newLocale)
                    LocaleList.setDefault(localeList)
                    configuration.setLocales(localeList)

                    return ContextWrapper(context.createConfigurationContext(configuration))

                }
                Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN -> {
                    configuration.setLocale(newLocale)
                    return ContextWrapper(context.createConfigurationContext(configuration))

                }
                else -> {
                    configuration.locale = newLocale
                    res.updateConfiguration(configuration, res.displayMetrics)
                }
            }

            return ContextWrapper(context)
        }
    }
}
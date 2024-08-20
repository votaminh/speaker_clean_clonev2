package com.msc.demo_mvvm.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.msc.demo_mvvm.R
import java.util.Locale


object AppEx {
    fun Activity.shareText(text : String){
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            this.getString(R.string.txt_share)
        )
        intent.putExtra(Intent.EXTRA_TEXT, text)
        startActivity(Intent.createChooser(
            intent,
            this.getString(R.string.txt_share)
        ))
    }

    fun Context.getDeviceLanguage(): String {
        return Locale.getDefault().language
    }

    fun Context.setAppLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    fun range(process: Int, start: Float, end: Float): Float {
        return (end - start) * process / 100 + start
    }

    fun invertRange(values: Float, start: Float, end: Float): Float {
        return (values - start) * 100 / (end - start)
    }
}
package com.msc.blower_clean.utils

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.msc.blower_clean.App
import com.msc.blower_clean.admob.NameRemoteAdmob
import org.json.JSONObject

class RemoteConfig {

    private val TAG = "remoteConfig"

    companion object {
        private var mInstance : RemoteConfig? = null

        fun instance(): RemoteConfig {
            if(mInstance == null){
                mInstance = RemoteConfig()
            }
            return mInstance as RemoteConfig
        }

    }

    fun fetch(success : (() -> Unit)? = null) {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                } else {

                }
                success?.invoke()
                updateConfig()
            }

        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                remoteConfig.activate().addOnCompleteListener {
                    updateConfig()
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {

            }
        })
    }

    private fun updateConfig() {
        kotlin.runCatching {
            val remoteConfig = Firebase.remoteConfig
            val adsConfig = remoteConfig.getString("admob_config")
            if(!adsConfig.isNullOrEmpty()){
                val adsJson = JSONObject(adsConfig)
                putBooleanToSP(adsJson, NameRemoteAdmob.inter_splash)
                putBooleanToSP(adsJson, NameRemoteAdmob.native_language)
                putBooleanToSP(adsJson, NameRemoteAdmob.native_onboarding)
            }
//            putBooleanToSP(remoteConfig, NameRemoteAdmob.INTER_CATEGORY)
//            putBooleanToSP(remoteConfig, NameRemoteAdmob.NATIVE_EXIT)
//            putBooleanToSP(remoteConfig, NameRemoteAdmob.BANNER_SPLASH)
//            putBooleanToSP(remoteConfig, NameRemoteAdmob.BANNER_ALL)
        }
    }

    private fun putBooleanToSP(json : JSONObject, name: String) {
        val spManager = App.instance?.applicationContext?.let { SpManager.getInstance(it) }
        val values = json.getBoolean(name)
        spManager?.putBoolean(name, values)
        Log.i(TAG, "$name : $values")
    }

}
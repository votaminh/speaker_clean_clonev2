package com.msc.blower_clean.utils

import android.annotation.SuppressLint
import com.msc.blower_clean.App
import com.msc.blower_clean.BuildConfig
import com.msc.blower_clean.admob.BaseAdmob
import com.msc.blower_clean.admob.NameRemoteAdmob
import com.msc.blower_clean.admob.NativeAdmob

class NativeAdmobUtils {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var languageNative1: NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var languageNative2: NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var onboardNativeAdmob1: NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var onboardNativeAdmob2: NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var onboardFullNativeAdmob : NativeAdmob? = null

        @SuppressLint("StaticFieldLeak")
        var permissionNative : NativeAdmob? = null

        fun loadNativeLanguage() {
            if (NetworkUtil.isOnline) {
                App.instance?.applicationContext?.let { context ->

                    if(SpManager.getInstance(context).getBoolean(NameRemoteAdmob.native_language, true)){

                        if(SpManager.getInstance(context).getBoolean("first_native_language", true)){
                            SpManager.getInstance(context).putBoolean("first_native_language", false)
                            languageNative1 = NativeAdmob(context, BuildConfig.native_language_1_1)
                            languageNative1?.load(null)

                            languageNative2 = NativeAdmob(context, BuildConfig.native_language_1_2)
                            languageNative2?.load(null)
                        }else{
                            languageNative1 = NativeAdmob(context, BuildConfig.native_language_2_1)
                            languageNative1?.load(null)

                            languageNative2 = NativeAdmob(context, BuildConfig.native_language_2_2)
                            languageNative2?.load(null)
                        }
                    }
                }
            }
        }


        fun loadNativeOnboard() {
            App.instance?.applicationContext?.let { context ->

                if(SpManager.getInstance(context).getBoolean(NameRemoteAdmob.native_onboarding, true)){
                    if(SpManager.getInstance(context).getBoolean("first_native_onboarding", true)){
                        SpManager.getInstance(context).putBoolean("first_native_onboarding", false)
                        onboardNativeAdmob1 = NativeAdmob(context, BuildConfig.native_onboarding_1_1)
                        onboardNativeAdmob1?.load(null)

                        onboardNativeAdmob2 = NativeAdmob(context, BuildConfig.native_onboarding_1_2)
                        onboardNativeAdmob2?.load(null)
                    }else{
                        onboardNativeAdmob1 = NativeAdmob(context, BuildConfig.native_onboarding_2_1)
                        onboardNativeAdmob1?.load(null)

                        onboardNativeAdmob2 = NativeAdmob(context, BuildConfig.native_onboarding_2_2)
                        onboardNativeAdmob2?.load(null)
                    }
                }

                if(SpManager.getInstance(context).getBoolean(NameRemoteAdmob.native_full_screen, true)){
                    if(SpManager.getInstance(context).getBoolean("first_native_full_onboarding", true)){
                        SpManager.getInstance(context).putBoolean("first_native_full_onboarding", false)
                        onboardFullNativeAdmob = NativeAdmob(context, BuildConfig.native_onboarding_full_2f)
                        onboardFullNativeAdmob?.load(object : BaseAdmob.OnAdmobLoadListener {
                            override fun onLoad() {
                            }
                            override fun onError(e: String?) {
                                onboardFullNativeAdmob = NativeAdmob(
                                    context,
                                    BuildConfig.native_onboarding_full
                                )
                                onboardFullNativeAdmob?.load(null)
                            }
                        })
                    }else{
                        onboardFullNativeAdmob = NativeAdmob(context, BuildConfig.native_onboarding_full_2)
                        onboardFullNativeAdmob?.load(null)
                    }
                }
            }
        }

        fun loadNativePermission() {
            if (NetworkUtil.isOnline) {
                App.instance?.applicationContext?.let { context ->
                    if(SpManager.getInstance(context).getBoolean("first_native_feature", true)){
                        SpManager.getInstance(context).putBoolean("first_native_feature", false)

                        permissionNative = NativeAdmob(context, BuildConfig.native_feature_2f)
                        permissionNative?.load(object : BaseAdmob.OnAdmobLoadListener {
                            override fun onLoad() {

                            }

                            override fun onError(e: String?) {
                                permissionNative = NativeAdmob(context, BuildConfig.native_freature)
                                permissionNative?.load(null)
                            }

                        })
                    }else{
                        permissionNative = NativeAdmob(context, BuildConfig.native_freature_2)
                        permissionNative?.load(null)
                    }
                }
            }
        }

    }


    fun loadNativeExit() {
//            App.instance?.applicationContext?.let {context ->
////                nativeExitLiveData = NativeAdmob(
////                    context,
////                    BuildConfig.native_exit
////                )
////                nativeExitLiveData?.load(null)
//            }
    }
}
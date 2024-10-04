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

        fun loadNativeLanguage() {
            if(NetworkUtil.isOnline){
                App.instance?.applicationContext?.let { context ->
                    SpManager.getInstance(context).putBoolean("first_native_language", false)

                    languageNative1 = NativeAdmob(
                        context,
                        BuildConfig.native_language_1_1
                    )
                    languageNative1?.load(object : BaseAdmob.OnAdmobLoadListener {
                        override fun onLoad() {
                        }

                        override fun onError(e: String?) {
                        }

                    })

                    languageNative2 = NativeAdmob(
                        context,
                        BuildConfig.native_language_1_2
                    )
                    languageNative2?.load(object : BaseAdmob.OnAdmobLoadListener {
                        override fun onLoad() {
                        }

                        override fun onError(e: String?) {
                        }

                    })
                }
                }
            }
        }
        fun loadNativePermission() {
//            App.instance?.applicationContext?.let { context ->
////                if(SpManager.getInstance(context).getBoolean(NameRemoteAdmob.NATIVE_PERMISSION, true) && NetworkUtil.isOnline){
////                    permissionNativeAdmob = NativeAdmob(
////                        context,
////                        BuildConfig.native_permission
////                    )
////                    permissionNativeAdmob?.load(null)
////                }
//            }
        }

        fun loadNativeOnboard() {
//            App.instance?.applicationContext?.let {context ->
////                onboardNativeAdmob1 = NativeAdmob(
////                    context,
////                    BuildConfig.native_onboarding
////                )
////                onboardNativeAdmob1?.load(null)
////
////                onboardNativeAdmob2 = NativeAdmob(
////                    context,
////                    BuildConfig.native_onboarding2
////                )
////                onboardNativeAdmob2?.load(null)
////
////                onboardNativeAdmob3 = NativeAdmob(
////                    context,
////                    BuildConfig.native_onboarding3
////                )
////                onboardNativeAdmob3?.load(null)
////
////                onboardFullNativeAdmob = NativeAdmob(context, BuildConfig.native_full_screen)
////                onboardFullNativeAdmob?.load(null)
//            }
        }

        fun loadNativeExit(){
//            App.instance?.applicationContext?.let {context ->
////                nativeExitLiveData = NativeAdmob(
////                    context,
////                    BuildConfig.native_exit
////                )
////                nativeExitLiveData?.load(null)
//            }
        }
}
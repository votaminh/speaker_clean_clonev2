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


        fun loadNativeOnboard() {
            App.instance?.applicationContext?.let {context ->
                SpManager.getInstance(context).putBoolean("first_native_onboarding", false)
                onboardNativeAdmob1 = NativeAdmob(
                    context,
                    BuildConfig.native_onboarding_1_1
                )

                onboardNativeAdmob1?.load(object : BaseAdmob.OnAdmobLoadListener {
                    override fun onLoad() {
                    }

                    override fun onError(e: String?) {
                    }

                })

                onboardNativeAdmob2 = NativeAdmob(
                    context,
                    BuildConfig.native_onboarding_1_2
                )
                onboardNativeAdmob2?.load(object : BaseAdmob.OnAdmobLoadListener {
                    override fun onLoad() {
                    }

                    override fun onError(e: String?) {
                    }

                })
            }
        }

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
package com.msc.blower_clean.component.onboarding

import com.msc.blower_clean.admob.NativeAdmob


data class OnBoarding(val resImage: Int, val resTitle: Int, val resDescription: Int, var nativeAdmob : NativeAdmob? = null){
    companion object {
        const val FULL_NATIVE_FLAG = 1822
    }
}

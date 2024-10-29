package com.msc.blower_clean.utils

import android.app.Activity
import com.msc.blower_clean.App
import com.msc.blower_clean.BuildConfig
import com.msc.blower_clean.admob.BaseAdmob
import com.msc.blower_clean.admob.InterAdmob
import com.msc.blower_clean.admob.NameRemoteAdmob

class InterNativeUtils {
    companion object {
        var interBack : InterAdmob? = null
        private var latestInterShow: Long = 0
        private var firstRequest = true

        fun loadInterBack(){
//            App.instance?.applicationContext?.let { context ->
//                if(SpManager.getInstance(context).getBoolean(NameRemoteAdmob.inter_back, true)){
//                    interBack = InterAdmob(
//                        context,
//                        BuildConfig.inter_back
//                    )
//                    interBack?.load(object : BaseAdmob.OnAdmobLoadListener {
//                        override fun onLoad() {
//                        }
//
//                        override fun onError(e: String?) {
//                        }
//
//                    })
//                }
//            }
        }

        fun showInterAction(activity : Activity, nextAction : (() -> Unit)? = null){
            nextAction?.invoke()
//            if(firstRequest){
//                firstRequest = false
//                nextAction?.invoke()
//                return
//            }
//
//            if(latestInterShow == 0L){
//                latestInterShow = System.currentTimeMillis()
//            }else if(System.currentTimeMillis() - latestInterShow < 30000){
//                nextAction?.invoke()
//                return
//            }
//
//            latestInterShow = System.currentTimeMillis()
//
//            if(interBack == null || !SpManager.getInstance(activity).getBoolean(NameRemoteAdmob.inter_back, true)){
//                nextAction?.invoke()
//            }else{
//                interBack?.showInterstitial(activity, object : BaseAdmob.OnAdmobShowListener{
//                    override fun onShow() {
//                        nextAction?.invoke()
//                        loadInterBack()
//                    }
//
//                    override fun onError(e: String?) {
//                        nextAction?.invoke()
//                        loadInterBack()
//                    }
//                })
//            }
        }
    }
}
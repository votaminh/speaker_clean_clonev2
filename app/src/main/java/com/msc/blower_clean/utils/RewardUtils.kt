package com.msc.blower_clean.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.msc.blower_clean.App
import com.msc.blower_clean.BuildConfig
import com.msc.blower_clean.admob.BaseAdmob
import com.msc.blower_clean.admob.RewardAdmob

class RewardUtils {
    companion object {
        @SuppressLint("StaticFieldLeak")
        var rewardAdmob : RewardAdmob? = null


        fun loadRewardFeature(context : Context){
            rewardAdmob = RewardAdmob(context, BuildConfig.reward_feature_2f)
            rewardAdmob?.load(object : BaseAdmob.OnAdmobLoadListener{
                override fun onLoad() {

                }

                override fun onError(e: String?) {
                    rewardAdmob = RewardAdmob(context, BuildConfig.reward_feature)
                    rewardAdmob?.load(null)
                }

            })
        }

        fun showRewardFeature(activity: Activity, nextAction : (() -> Unit)? = null){
            rewardAdmob?.show(activity, object : BaseAdmob.OnAdmobShowListener{
                override fun onShow() {
                    nextAction?.invoke()
                    rewardAdmob?.load(null)
                }

                override fun onError(e: String?) {
                    nextAction?.invoke()
                    rewardAdmob?.load(null)
                }
            })
        }

    }
}
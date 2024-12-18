package com.msc.blower_clean.component.setting

import android.app.Activity
import android.content.Intent
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.language.LanguageActivity
import com.msc.blower_clean.databinding.ActivitySettingCone2Binding
import com.msc.blower_clean.utils.AppEx.openAppInStore
import com.msc.blower_clean.utils.AppEx.showPolicyApp
import com.msc.blower_clean.utils.InterNativeUtils

class SettingActivity : BaseActivity<ActivitySettingCone2Binding>() {

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, SettingActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivitySettingCone2Binding {
        return ActivitySettingCone2Binding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        viewBinding.run {
            imvBack.setOnClickListener {
                InterNativeUtils.showInterAction(this@SettingActivity){
                    finish()
                }
            }
            btnLanguage.setOnClickListener {
                startActivity(
                    Intent(
                        this@SettingActivity,
                        LanguageActivity::class.java
                    )
                )
            }
            btnPrivacyPolicy.setOnClickListener { showPolicyApp() }
            btnRateUs.setOnClickListener { openAppInStore() }
        }
    }
}
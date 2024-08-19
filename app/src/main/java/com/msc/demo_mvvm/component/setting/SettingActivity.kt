package com.msc.demo_mvvm.component.setting

import android.app.Activity
import android.content.Intent
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.databinding.ActivitySettingBinding

class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, SettingActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        viewBinding.run {
            imvBack.setOnClickListener {
                finish()
            }
        }
    }
}
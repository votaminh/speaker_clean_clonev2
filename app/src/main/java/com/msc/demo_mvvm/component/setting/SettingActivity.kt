package com.msc.demo_mvvm.component.setting

import android.app.Activity
import android.content.Intent
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.databinding.ActivitySettingCone2Binding

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
                finish()
            }
        }
    }
}
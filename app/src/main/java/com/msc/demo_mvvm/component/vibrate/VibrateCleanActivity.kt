package com.msc.demo_mvvm.component.vibrate

import android.app.Activity
import android.content.Intent
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.databinding.ActivityVibrateBinding

class VibrateCleanActivity : BaseActivity<ActivityVibrateBinding>() {

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, VibrateCleanActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityVibrateBinding {
        return ActivityVibrateBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
    }
}
package com.msc.demo_mvvm.component.vibrate

import android.app.Activity
import android.content.Intent
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.databinding.ActivityVibrateClone2Binding

class VibrateCleanActivity : BaseActivity<ActivityVibrateClone2Binding>() {

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, VibrateCleanActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityVibrateClone2Binding {
        return ActivityVibrateClone2Binding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
    }
}
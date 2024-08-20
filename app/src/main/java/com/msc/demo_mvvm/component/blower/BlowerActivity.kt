package com.msc.demo_mvvm.component.blower

import android.app.Activity
import android.content.Intent
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.databinding.ActivityBlowerClone2Binding

class BlowerActivity : BaseActivity<ActivityBlowerClone2Binding>() {

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, BlowerActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityBlowerClone2Binding {
        return ActivityBlowerClone2Binding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
    }
}
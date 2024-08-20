package com.msc.demo_mvvm.component.manual

import android.app.Activity
import android.content.Intent
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.databinding.ActivityManualCleanerClone2Binding

class ManualCleanerActivity : BaseActivity<ActivityManualCleanerClone2Binding>() {

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, ManualCleanerActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityManualCleanerClone2Binding {
        return ActivityManualCleanerClone2Binding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
    }
}
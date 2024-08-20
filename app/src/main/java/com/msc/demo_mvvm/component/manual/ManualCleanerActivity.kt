package com.msc.demo_mvvm.component.manual

import android.app.Activity
import android.content.Intent
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.databinding.ActivityManualCleanerBinding

class ManualCleanerActivity : BaseActivity<ActivityManualCleanerBinding>() {

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, ManualCleanerActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityManualCleanerBinding {
        return ActivityManualCleanerBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
    }
}
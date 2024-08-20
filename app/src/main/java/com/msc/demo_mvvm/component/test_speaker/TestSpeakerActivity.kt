package com.msc.demo_mvvm.component.test_speaker

import android.app.Activity
import android.content.Intent
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.databinding.ActivityTestSpeakerClone2Binding

class TestSpeakerActivity : BaseActivity<ActivityTestSpeakerClone2Binding>() {

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, TestSpeakerActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityTestSpeakerClone2Binding {
        return ActivityTestSpeakerClone2Binding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
    }

}
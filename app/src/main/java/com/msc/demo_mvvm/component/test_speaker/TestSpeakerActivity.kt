package com.msc.demo_mvvm.component.test_speaker

import android.app.Activity
import android.content.Intent
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.databinding.ActivityTestSpeakerBinding

class TestSpeakerActivity : BaseActivity<ActivityTestSpeakerBinding>() {

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, TestSpeakerActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityTestSpeakerBinding {
        return ActivityTestSpeakerBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
    }

}
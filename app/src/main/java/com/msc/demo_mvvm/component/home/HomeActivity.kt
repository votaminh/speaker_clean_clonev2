package com.msc.demo_mvvm.component.home

import android.app.Activity
import android.content.Intent
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.component.auto.AutoCleanActivity
import com.msc.demo_mvvm.component.setting.SettingActivity
import com.msc.demo_mvvm.databinding.ActivityMainBinding
import com.msc.demo_mvvm.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityMainBinding>() {

    @Inject
    lateinit var spManager: SpManager

    companion object {
        const val REQUEST_PICKER_CONTACT = 211
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        spManager.saveOnBoarding()

        viewBinding.run {
            setting.setOnClickListener {
                SettingActivity.start(this@HomeActivity)
            }
            auto.setOnClickListener {
                AutoCleanActivity.start(this@HomeActivity)
            }
            manual.setOnClickListener {
                showToast("manual")
            }
            vibrate.setOnClickListener {
                showToast("vibrate")
            }
            blower.setOnClickListener {
                showToast("blower")
            }
        }
    }
}
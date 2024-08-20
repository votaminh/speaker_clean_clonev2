package com.msc.demo_mvvm.component.ump

import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.component.splash.SplashActivity
import com.msc.demo_mvvm.App
import com.msc.demo_mvvm.databinding.ActivitySplashClone2Binding
import com.msc.demo_mvvm.utils.RemoteConfig
import com.msc.demo_mvvm.utils.SpManager
import com.msc.demo_mvvm.utils.UMPUtils


class UMPActivity : BaseActivity<ActivitySplashClone2Binding>() {
    private val TAG = "ump_activity"

    override fun provideViewBinding(): ActivitySplashClone2Binding = ActivitySplashClone2Binding.inflate(layoutInflater)

    override fun initData() {
        super.initData()

        if(SpManager.getInstance(this).isUMPShowed()){
            RemoteConfig.instance().fetch()
            openSplash();
        }else{
            RemoteConfig.instance().fetch{
                initUmp()
            }
        }
    }

    private fun openSplash() {

        val app : App = application as App
        app.initAds()

        SpManager.getInstance(this).setUMPShowed(true)
        SplashActivity.start(this);
        finish()
    }

    private fun initUmp() {
        UMPUtils.init(this@UMPActivity, nextAction = {
            openSplash()
        }, true, false)
    }
}
package com.msc.blower_clean.component.ump

import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.splash.SplashActivity
import com.msc.blower_clean.App
import com.msc.blower_clean.databinding.ActivitySplashClone2Binding
import com.msc.blower_clean.utils.RemoteConfig
import com.msc.blower_clean.utils.SpManager
import com.msc.blower_clean.utils.UMPUtils


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
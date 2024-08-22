package com.msc.blower_clean.component.home

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.msc.blower_clean.BuildConfig
import com.msc.blower_clean.admob.BannerAdmob
import com.msc.blower_clean.admob.CollapsiblePositionType
import com.msc.blower_clean.admob.NameRemoteAdmob
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.auto.AutoCleanActivity
import com.msc.blower_clean.component.blower.BlowerActivity
import com.msc.blower_clean.component.manual.ManualCleanerActivity
import com.msc.blower_clean.component.setting.SettingActivity
import com.msc.blower_clean.component.vibrate.VibrateCleanActivity
import com.msc.blower_clean.databinding.ActivityMainClone2Binding
import com.msc.blower_clean.utils.SpManager
import com.msc.blower_clean.utils.UtilRate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityMainClone2Binding>() {

    @Inject
    lateinit var spManager: SpManager

    companion object {
        const val REQUEST_PICKER_CONTACT = 211
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityMainClone2Binding {
        return ActivityMainClone2Binding.inflate(layoutInflater)
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
                ManualCleanerActivity.start(this@HomeActivity)
            }
            vibrate.setOnClickListener {
                VibrateCleanActivity.start(this@HomeActivity)
            }
            blower.setOnClickListener {
                BlowerActivity.start(this@HomeActivity)
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        })


        showBanner()
    }

    private fun showBanner() {
        if(SpManager.getInstance(this).getBoolean(NameRemoteAdmob.BANNER_COLAPSE, true)){
            val bannerAdmob = BannerAdmob(this, CollapsiblePositionType.BOTTOM)
            bannerAdmob.showBanner(this@HomeActivity, BuildConfig.banner_collap_home, viewBinding.banner)
        }else{
            viewBinding.banner.visibility = View.GONE
        }
    }

    override fun onBack() {
        if(!UtilRate.showDialogRate(this@HomeActivity)){
            finish()
        }
    }
}
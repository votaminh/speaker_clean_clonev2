package com.msc.blower_clean.component.home

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.msc.blower_clean.BuildConfig
import com.msc.blower_clean.admob.BannerAdmob
import com.msc.blower_clean.admob.BaseAdmob
import com.msc.blower_clean.admob.CollapsiblePositionType
import com.msc.blower_clean.admob.InterAdmob
import com.msc.blower_clean.admob.NameRemoteAdmob
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.auto.AutoCleanActivity
import com.msc.blower_clean.component.blower.BlowerActivity
import com.msc.blower_clean.component.manual.ManualCleanerActivity
import com.msc.blower_clean.component.setting.SettingActivity
import com.msc.blower_clean.component.vibrate.VibrateCleanActivity
import com.msc.blower_clean.databinding.ActivityMainClone2Binding
import com.msc.blower_clean.utils.InterNativeUtils
import com.msc.blower_clean.utils.SpManager
import com.msc.blower_clean.utils.UtilRate
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityMainClone2Binding>() {

    @Inject
    lateinit var spManager: SpManager

    private var interAdmob : InterAdmob? = null
    private var latestInterShow: Long = 0
    private var firstRequest = true

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
                showInterAction{
                    SettingActivity.start(this@HomeActivity)
                }
            }
            auto.setOnClickListener {
                showInterAction{
                    AutoCleanActivity.start(this@HomeActivity)
                }
            }
            manual.setOnClickListener {
                showInterAction{
                    ManualCleanerActivity.start(this@HomeActivity)
                }
            }
            vibrate.setOnClickListener {
                showInterAction{
                    VibrateCleanActivity.start(this@HomeActivity)
                }
            }
            blower.setOnClickListener {
                showInterAction{
                    BlowerActivity.start(this@HomeActivity)
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack()
            }
        })

        loadInter()
        showBanner()
        InterNativeUtils.loadInterBack()
    }

    private fun showBanner() {
        if(SpManager.getInstance(this).getBoolean(NameRemoteAdmob.banner_home, true)){
            val bannerAdmob = BannerAdmob(this, CollapsiblePositionType.BOTTOM)
            bannerAdmob.showBanner(this@HomeActivity, BuildConfig.banner_home, viewBinding.banner)
        }else{
            viewBinding.banner.visibility = View.GONE
        }
    }

    private fun loadInter() {
        if(spManager.getBoolean(NameRemoteAdmob.inter_home, true)){
            interAdmob = InterAdmob(this, BuildConfig.inter_home)
            interAdmob?.load(object : BaseAdmob.OnAdmobLoadListener {
                override fun onLoad() {
                }

                override fun onError(e: String?) {
                }
            })
        }
    }

    fun showInterAction(nextAction : (() -> Unit)? = null){
        if(firstRequest){
            firstRequest = false
            nextAction?.invoke()
            return
        }

        if(latestInterShow == 0L){
            latestInterShow = System.currentTimeMillis()
        }else if(System.currentTimeMillis() - latestInterShow < 30000){
            nextAction?.invoke()
            return
        }

        latestInterShow = System.currentTimeMillis()

        if(interAdmob == null || !spManager.getBoolean(NameRemoteAdmob.inter_home, true)){
            nextAction?.invoke()
        }else{
            interAdmob?.showInterstitial(this, object : BaseAdmob.OnAdmobShowListener{
                override fun onShow() {
                    nextAction?.invoke()
                    loadInter()
                }

                override fun onError(e: String?) {
                    nextAction?.invoke()
                    loadInter()
                }

            })
        }
    }

    override fun onBack() {
        if(!UtilRate.showDialogRate(this@HomeActivity)){
            finish()
        }
    }
}
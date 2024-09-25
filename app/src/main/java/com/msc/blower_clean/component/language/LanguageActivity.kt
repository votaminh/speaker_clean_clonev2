package com.msc.blower_clean.component.language

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.nativead.NativeAd
import com.msc.blower_clean.admob.NameRemoteAdmob
import com.msc.blower_clean.base.activity.BaseActivity
import com.flash.light.component.language.LanguageAdapter
import com.msc.blower_clean.component.onboarding.OnBoardingActivity
import com.msc.blower_clean.R
import com.msc.blower_clean.admob.NativeAdmob
import com.msc.blower_clean.component.home.HomeActivity
import com.msc.blower_clean.databinding.ActivityLanguageClone2Binding
import com.msc.blower_clean.utils.AppEx.setAppLanguage
import com.msc.blower_clean.utils.NativeAdmobUtils
import com.msc.blower_clean.utils.NetworkUtil
import com.msc.blower_clean.utils.SpManager
import com.msc.blower_clean.utils.ViewEx.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageActivity : BaseActivity<ActivityLanguageClone2Binding>() {
    private val viewModel: LanguageViewModel by viewModels()
    private val languageAdapter = LanguageAdapter()

    @Inject
    lateinit var spManager: SpManager

    override fun provideViewBinding(): ActivityLanguageClone2Binding {
        return ActivityLanguageClone2Binding.inflate(layoutInflater)
    }

    override fun initViews() {
        val fromSplash = intent.getBooleanExtra(KEY_FROM_SPLASH, false)
        if (!fromSplash){
            viewBinding.imvBack.visibility = View.VISIBLE
        }else{
            viewBinding.imvBack.visibility = View.GONE
        }

        setStatusBarColor(ContextCompat.getColor(this, R.color.white), true)

        viewBinding.imvBack.setOnClickListener {
            finish()
        }

        viewBinding.rclLanguage.adapter = languageAdapter
        languageAdapter.onClick = {
            languageAdapter.selectLanguage(it.languageCode)
            showNativeS2()
            viewBinding.ivDone.visible()
        }
        viewBinding.ivDone.setOnClickListener {
            languageAdapter.selectedLanguage()?.let { languageModel ->
                spManager.saveLanguage(languageModel)
                setAppLanguage(languageModel.languageCode)
                val fromSplash = intent.getBooleanExtra(KEY_FROM_SPLASH, false)
                if (fromSplash) {
                    OnBoardingActivity.start(this)
                    finish()
                } else {
                    startActivity(Intent(this, HomeActivity::class.java).also {
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    })
                }
            }
        }

        if(spManager.getShowOnBoarding()){
            viewBinding.ivDone.setText(R.string.txt_next)
        }

        if (isTempNativeAd != null) {
            isTempNativeAd = null
        }

        if (spManager.getShowOnBoarding() && NetworkUtil.isOnline) {
            if (spManager.getBoolean(NameRemoteAdmob.NATIVE_ONBOARD, true)) {
                NativeAdmobUtils.loadNativeOnboard()
            }
        }
    }

    private fun showNativeS2() {
        NativeAdmobUtils.languageNativeAdmob2Floor?.let { s2Native ->
            if(s2Native.available()){
                s2Native.nativeAdLive.observe(this@LanguageActivity){
                    checkShowNative(s2Native)
                }
            }
        }
    }

    override fun initObserver() {
        viewModel.listLanguage.observe(this) {
            languageAdapter.setData(ArrayList(it))
        }

        viewBinding.flAdplaceholder.visibility = View.GONE

        if(NativeAdmobUtils.languageNativeAdmobDefault == null && spManager.getBoolean(NameRemoteAdmob.NATIVE_LANGUAGE, true)){
            NativeAdmobUtils.loadNativeLanguage()
        }

        NativeAdmobUtils.languageNativeAdmobDefault?.run {
            nativeAdLive?.observe(this@LanguageActivity){
                checkShowNative(this)
            }
        }
    }

    private fun checkShowNative(nativeAdmob: NativeAdmob) {
        if(nativeAdmob.available() && spManager.getBoolean(NameRemoteAdmob.NATIVE_LANGUAGE, true)){
            viewBinding.flAdplaceholder.visibility = View.VISIBLE
            nativeAdmob.showNative(viewBinding.flAdplaceholder, null)
        }
    }

    override fun initData() {
        viewModel.loadListLanguage()
    }

    override fun onDestroy() {
        isTempNativeAd = null
        super.onDestroy()
    }

    companion object {
        var isTempNativeAd: NativeAd? = null
        const val KEY_FROM_SPLASH = "key_splash"
        fun start(context: Context, fromSplash: Boolean = false) {
            Intent(context, LanguageActivity::class.java).also {
                it.putExtra(KEY_FROM_SPLASH, fromSplash)
                context.startActivity(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        NativeAdmobUtils.languageNativeAdmob2Floor?.reLoad()
    }
}
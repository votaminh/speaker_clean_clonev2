package com.msc.blower_clean.component.language

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.facebook.shimmer.ShimmerFrameLayout
import com.flash.light.component.language.LanguageAdapter
import com.msc.blower_clean.R
import com.msc.blower_clean.admob.BaseAdmob
import com.msc.blower_clean.admob.NameRemoteAdmob
import com.msc.blower_clean.admob.NativeAdmob
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.home.HomeActivity
import com.msc.blower_clean.component.onboarding.OnBoardingActivity
import com.msc.blower_clean.databinding.ActivityLanguageClone2Binding
import com.msc.blower_clean.domain.layer.LanguageModel
import com.msc.blower_clean.utils.Constant
import com.msc.blower_clean.utils.LocaleHelper
import com.msc.blower_clean.utils.NativeAdmobUtils
import com.msc.blower_clean.utils.SpManager
import com.msc.blower_clean.utils.ViewEx.gone
import com.msc.blower_clean.utils.ViewEx.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageActivity : BaseActivity<ActivityLanguageClone2Binding>() {
    private var selectLanguageModel: LanguageModel? = null
    private val viewModel: LanguageViewModel by viewModels()
    private val languageAdapter = LanguageAdapter()

    @Inject
    lateinit var spManager: SpManager

    override fun provideViewBinding(): ActivityLanguageClone2Binding {
        return ActivityLanguageClone2Binding.inflate(layoutInflater)
    }

    override fun initViews() {

        setStatusBarColor(ContextCompat.getColor(this, R.color.white), true)

        val isFromSplash = intent.getBooleanExtra(Constant.KEY_INTENT_FROM_SPLASH, false)

        viewBinding.ivDone.gone()
        viewBinding.rclLanguage.adapter = languageAdapter
        languageAdapter.onClick = {languageModel ->
            viewBinding.ivDone.visible()
            selectLanguageModel = languageModel
            languageAdapter.selectLanguage(languageModel.languageCode)
            showNative2()
        }

        if(isFromSplash){
            viewBinding.imvBack.gone()
        }else{
            viewBinding.imvBack.visible()
        }

        viewBinding.imvBack.setOnClickListener {
            finish()
        }

        viewBinding.ivDone.setOnClickListener {
            if (selectLanguageModel == null) {
                selectLanguageModel = languageAdapter.dataSet.find { it.selected }
            }
            selectLanguageModel?.let { it1 -> spManager.saveLanguage(it1) }
            LocaleHelper.setLocale(this@LanguageActivity, language = selectLanguageModel?.languageCode ?: "en")
            if (isFromSplash) {
                OnBoardingActivity.start(this)
                finish()
            } else {
                HomeActivity.start(this@LanguageActivity)
                finish()
            }
        }

        NativeAdmobUtils.loadNativeOnboard()
    }

    private fun showNative2() {
        viewBinding.flAdplaceholder1.root.gone()
        viewBinding.flAdplaceholder2.root.visible()

        NativeAdmobUtils.languageNative2?.run {
            nativeAdLive?.observe(this@LanguageActivity){
                if(available()){
                    showNativeAd(this, viewBinding.flAdplaceholder2.root)
                }
            }
        }
    }

    override fun initObserver() {
        viewModel.listLanguage.observe(this) {
            languageAdapter.setData(ArrayList(it))
//            languageAdapter.selectLanguage(spManager.getLanguage().languageCode)
        }

        NativeAdmobUtils.languageNative1?.run {
            nativeAdLive?.observe(this@LanguageActivity){
                if(available()){
                    showNativeAd(this, viewBinding.flAdplaceholder1.root)
                }
            }
        }
    }

    private fun showNativeAd(nativeAdmob: NativeAdmob, parent : ShimmerFrameLayout) {
        if(spManager.getBoolean(NameRemoteAdmob.native_language, true)){
            nativeAdmob.showNative(parent, object : BaseAdmob.OnAdmobShowListener {
                override fun onShow() {
                }

                override fun onError(e: String?) {
                }

            })
            parent.visible()
        }else{
            parent.gone()
        }
    }

    override fun initData() {
        viewModel.loadListLanguage()
    }

    companion object {
        fun start(activity: Activity, fromSplash : Boolean) {
            val intent = Intent(activity, LanguageActivity::class.java)
            intent.putExtra(Constant.KEY_INTENT_FROM_SPLASH, fromSplash)
            activity.startActivity(intent)
        }
    }
}
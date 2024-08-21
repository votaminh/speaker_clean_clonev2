package com.msc.blower_clean.component.splash

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.view.View
import com.msc.blower_clean.admob.NameRemoteAdmob
import com.msc.blower_clean.BuildConfig
import com.msc.blower_clean.admob.BannerAdmob
import com.msc.blower_clean.admob.BaseAdmob
import com.msc.blower_clean.admob.CollapsiblePositionType
import com.msc.blower_clean.admob.InterAdmob
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.home.HomeActivity
import com.msc.blower_clean.component.language.LanguageActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import com.msc.blower_clean.databinding.ActivitySplashClone2Binding
import com.msc.blower_clean.utils.NativeAdmobUtils
import com.msc.blower_clean.utils.NetworkUtil
import com.msc.blower_clean.utils.SpManager

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashClone2Binding>() {
    private var progressAnimator: ValueAnimator? = null

    @Inject
    lateinit var spManager: SpManager

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, SplashActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivitySplashClone2Binding =
        ActivitySplashClone2Binding.inflate(layoutInflater)

    override fun onDestroy() {
        cancelLoadingListener()
        super.onDestroy()
    }

    private fun cancelLoadingListener() {
        progressAnimator?.removeAllListeners()
        progressAnimator?.cancel()
        progressAnimator = null
    }

    override fun onResume() {
        super.onResume()
        if (progressAnimator?.isPaused == true) {
            progressAnimator?.resume()
        }
    }

    override fun onPause() {
        progressAnimator?.pause()
        super.onPause()
    }

    override fun initViews() {
        if (spManager.getShowOnBoarding() && NetworkUtil.isOnline) {
            if (spManager.getBoolean(NameRemoteAdmob.NATIVE_LANGUAGE, true)) {
                NativeAdmobUtils.loadNativeLanguage()
            }
        }

        runProgress()
    }

    private fun runProgress() {

        showBanner()

        if (spManager.getBoolean(NameRemoteAdmob.INTER_SPLASH, true)) {
            val interAdmob = InterAdmob(this@SplashActivity, BuildConfig.inter_splash)
            interAdmob.load(object : BaseAdmob.OnAdmobLoadListener {
                override fun onLoad() {
                    if (spManager.getBoolean(NameRemoteAdmob.INTER_SPLASH, true)) {
                        interAdmob.showInterstitial(
                            this@SplashActivity,
                            object : BaseAdmob.OnAdmobShowListener {
                                override fun onShow() {
                                    gotoMainScreen()
                                }

                                override fun onError(e: String?) {
                                    gotoMainScreen()
                                }
                            })
                    } else {
                        gotoMainScreen()
                    }
                }

                override fun onError(e: String?) {
                    gotoMainScreen()
                }
            })
        } else {
            gotoMainScreen()
        }
    }

    private fun showBanner() {
        if(spManager.getBoolean(NameRemoteAdmob.BANNER_SPLASH, true)){
            val bannerAdmob = BannerAdmob(this, CollapsiblePositionType.NONE)
            bannerAdmob.showBanner(this@SplashActivity, BuildConfig.banner_splash, viewBinding.banner)
        }else{
            viewBinding.banner.visibility = View.GONE
        }
    }

    private fun gotoMainScreen() {
        cancelLoadingListener()
        if (spManager.getShowOnBoarding()) {
            LanguageActivity.start(this, true)
        } else {
            startActivity(Intent(this, HomeActivity::class.java))
        }
        finish()
    }
}
package com.msc.blower_clean.component.splash

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import com.msc.blower_clean.BuildConfig
import com.msc.blower_clean.admob.BaseAdmob
import com.msc.blower_clean.admob.BaseAdmob.OnAdmobShowListener
import com.msc.blower_clean.admob.InterAdmob
import com.msc.blower_clean.admob.NameRemoteAdmob
import com.msc.blower_clean.admob.OpenAdmob
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.home.HomeActivity
import com.msc.blower_clean.component.language.LanguageActivity
import com.msc.blower_clean.databinding.ActivitySplashClone2Binding
import com.msc.blower_clean.utils.NativeAdmobUtils
import com.msc.blower_clean.utils.NetworkUtil
import com.msc.blower_clean.utils.SpManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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
            if (spManager.getBoolean(NameRemoteAdmob.native_language, true)) {
                NativeAdmobUtils.loadNativeLanguage()
            }
        }

        runProgress()
    }

    private fun runProgress() {
        showBanner()
        if (spManager.getBoolean(NameRemoteAdmob.inter_splash, true)) {
            loadShowOpenAds(successAction = {
                gotoMainScreen()
            }, failAction = {
                loadShowInter(successAction = {
                    gotoMainScreen()
                }, failAction = {
                    gotoMainScreen()
                })
            })
        } else {
            gotoMainScreen()
        }
    }

    private fun loadShowInter(successAction : (() -> Unit)? = null, failAction : (() -> Unit)? = null) {
        val interAdmob = InterAdmob(this@SplashActivity, BuildConfig.inter_splash)
        interAdmob.load(object : BaseAdmob.OnAdmobLoadListener {
            override fun onLoad() {
                interAdmob.showInterstitial(this@SplashActivity,object : OnAdmobShowListener{
                    override fun onShow() {
                        successAction?.invoke()
                    }

                    override fun onError(e: String?) {
                        failAction?.invoke()
                    }

                })
            }

            override fun onError(e: String?) {
                failAction?.invoke()
            }
        })
    }

    private fun loadShowOpenAds(successAction : (() -> Unit)? = null, failAction : (() -> Unit)? = null) {
        val interAdmob = InterAdmob(this@SplashActivity, BuildConfig.inter_splash_high)
        interAdmob.load(object : BaseAdmob.OnAdmobLoadListener {
            override fun onLoad() {
                interAdmob.showInterstitial(this@SplashActivity,object : OnAdmobShowListener{
                    override fun onShow() {
                        successAction?.invoke()
                    }

                    override fun onError(e: String?) {
                        failAction?.invoke()
                    }

                })
            }

            override fun onError(e: String?) {
                failAction?.invoke()
            }
        })
    }

    private fun showBanner() {
//        if(spManager.getBoolean(NameRemoteAdmob.BANNER_SPLASH, true)){
//            val bannerAdmob = BannerAdmob(this, CollapsiblePositionType.NONE)
//            bannerAdmob.showBanner(this@SplashActivity, BuildConfig.banner_splash, viewBinding.banner)
//        }else{
//            viewBinding.banner.visibility = View.GONE
//        }
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
package com.msc.blower_clean.component.onboarding

import android.content.Context
import android.content.Intent
import android.os.Looper
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.msc.blower_clean.R
import com.msc.blower_clean.admob.BaseAdmob
import com.msc.blower_clean.admob.NameRemoteAdmob
import com.msc.blower_clean.admob.NativeAdmob
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.permission.PermissionActivity
import com.msc.blower_clean.databinding.ActivityOnboardingClone2Binding
import com.msc.blower_clean.utils.NativeAdmobUtils
import com.msc.blower_clean.utils.SpManager
import com.msc.blower_clean.utils.ViewEx.gone
import com.msc.blower_clean.utils.ViewEx.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnboardingClone2Binding>() {

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, OnBoardingActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityOnboardingClone2Binding =
        ActivityOnboardingClone2Binding.inflate(layoutInflater)

    private val viewModel: OnBoardingViewModel by viewModels()

    private val onBoardingAdapter = OnBoardingAdapter()
    private var currentPosition = 0

    override fun initViews() {
        viewBinding.apply {
            vpOnBoarding.adapter = onBoardingAdapter

            setStatusBarColor(ContextCompat.getColor(this@OnBoardingActivity, R.color.white), true)
            vpOnBoarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPosition = position

                    showNative(currentPosition)
                }
            })

            buttonNext.setOnClickListener {
                if (currentPosition < viewModel.listOnBoarding.size - 1) {
                    vpOnBoarding.setCurrentItem(currentPosition + 1, true)
                } else {
                    PermissionActivity.start(this@OnBoardingActivity)
                    finish()
                }
            }

            dotIndicator.attachTo(vpOnBoarding)
            onBoardingAdapter.setData(ArrayList(viewModel.listOnBoarding))
        }

        if(SpManager.getInstance(this).getBoolean(NameRemoteAdmob.native_onboarding, true)){
            NativeAdmobUtils.loadNativePermission()
        }
    }

    override fun initObserver() {
        super.initObserver()

        NativeAdmobUtils.onboardFullNativeAdmob?.run {
            nativeAdLive.observe(this@OnBoardingActivity){
                if(available() && SpManager.getInstance(context).getBoolean(NameRemoteAdmob.native_language, true)){
                    addAdsToOnboard(this)
                }
            }
        }
    }

    private fun addAdsToOnboard(it: NativeAdmob) {
        val adsOnboard = OnBoarding(
            OnBoarding.FULL_NATIVE_FLAG,
            OnBoarding.FULL_NATIVE_FLAG,
            OnBoarding.FULL_NATIVE_FLAG,
            it
        )

        onBoardingAdapter.getListData().add(2, adsOnboard)
        onBoardingAdapter.notifyDataSetChanged()
    }

    private fun showNative(currentPosition: Int) {
        viewBinding.flAdplaceholder.gone()
        viewBinding.navigationLayout.visible()

        if(!SpManager.getInstance(this@OnBoardingActivity).getBoolean(NameRemoteAdmob.native_language, true)){
            return
        }

        if(onBoardingAdapter.itemCount == 3){
            when(currentPosition){
                0 -> {
                    viewBinding.flAdplaceholder.visible()
                    NativeAdmobUtils.onboardNativeAdmob1?.run {
                        nativeAdLive?.observe(this@OnBoardingActivity){
                            if(available()){
                                android.os.Handler(Looper.getMainLooper()).postDelayed({
                                    showNative(viewBinding.flAdplaceholder, object : BaseAdmob.OnAdmobShowListener {
                                        override fun onShow() {
                                        }

                                        override fun onError(e: String?) {
                                        }

                                    })
                                }, 100)
                            }
                        }
                    }
                }

                1 -> {
                    viewBinding.flAdplaceholder.visible()
                    NativeAdmobUtils.onboardNativeAdmob2?.run {
                        nativeAdLive?.observe(this@OnBoardingActivity){
                            if(available()){
                                android.os.Handler(Looper.getMainLooper()).postDelayed({
                                    showNative(viewBinding.flAdplaceholder, object : BaseAdmob.OnAdmobShowListener {
                                        override fun onShow() {
                                        }

                                        override fun onError(e: String?) {
                                        }

                                    })
                                }, 100)
                            }
                        }
                    }
                }
            }
        }else {
            when(currentPosition){
                0 -> {
                    viewBinding.flAdplaceholder.visible()
                    NativeAdmobUtils.onboardNativeAdmob1?.run {
                        nativeAdLive?.observe(this@OnBoardingActivity){
                            if(available()){
                                android.os.Handler(Looper.getMainLooper()).postDelayed({
                                    showNative(viewBinding.flAdplaceholder, object : BaseAdmob.OnAdmobShowListener {
                                        override fun onShow() {
                                        }

                                        override fun onError(e: String?) {
                                        }

                                    })
                                }, 100)
                            }
                        }
                    }
                }

                2 -> {
                    viewBinding.navigationLayout.gone()
                }

                1 -> {
                    viewBinding.flAdplaceholder.visible()
                    NativeAdmobUtils.onboardNativeAdmob2?.run {
                        nativeAdLive?.observe(this@OnBoardingActivity){
                            if(available()){
                                android.os.Handler(Looper.getMainLooper()).postDelayed({
                                    showNative(viewBinding.flAdplaceholder, object : BaseAdmob.OnAdmobShowListener {
                                        override fun onShow() {
                                        }

                                        override fun onError(e: String?) {
                                        }

                                    })
                                }, 100)
                            }
                        }
                    }
                }
            }
        }
    }
}
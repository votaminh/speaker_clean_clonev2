package com.msc.blower_clean.component.onboarding

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.msc.blower_clean.admob.NameRemoteAdmob
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.R
import com.msc.blower_clean.admob.NativeAdmob
import com.msc.blower_clean.component.permission.PermissionActivity
import com.msc.blower_clean.databinding.ActivityOnboardingClone2Binding
import com.msc.blower_clean.utils.NativeAdmobUtils
import com.msc.blower_clean.utils.NetworkUtil
import com.msc.blower_clean.utils.SpManager
import com.msc.blower_clean.utils.ViewEx.gone
import com.msc.blower_clean.utils.ViewEx.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity<ActivityOnboardingClone2Binding>() {

    @Inject
    lateinit var spManager: SpManager

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, OnBoardingActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityOnboardingClone2Binding =
        ActivityOnboardingClone2Binding.inflate(layoutInflater)

    private val onBoardingAdapter = OnBoardingAdapter()
    private var currentPosition = 0

    private var currentNativeAdmob : NativeAdmob? = null
    override var isReloadInter: Boolean = false

    override fun initViews() {
        viewBinding.apply {
            vpOnBoarding.adapter = onBoardingAdapter

            vpOnBoarding.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    currentPosition = position

                    if(currentPosition == 2){
                        viewBinding.buttonNext.setText(R.string.txt_get_start)
                    }else{
                        viewBinding.buttonNext.setText(R.string.txt_next)
                    }

                    switchAds()
                }
            })

            buttonNext.setOnClickListener {
                if (currentPosition < onBoardingAdapter.getListData().size - 1) {
                    vpOnBoarding.setCurrentItem(currentPosition + 1, true)
                } else {
                    if(NetworkUtil.isOnline){
                        NativeAdmobUtils.loadNativePermission()
                    }
                    PermissionActivity.start(this@OnBoardingActivity)
                    finish()
                }
            }

            dotIndicator.attachTo(vpOnBoarding)
            onBoardingAdapter.setData(listOf(
                OnBoarding(
                    R.drawable.ic_onboarding1,
                    R.string.onboarding_title1,
                    R.string.onboarding_intro1
                ),
                OnBoarding(
                    R.drawable.ic_onboarding2,
                    R.string.onboarding_title2,
                    R.string.onboarding_intro2
                ),
                OnBoarding(
                    R.drawable.ic_onboarding3,
                    R.string.onboarding_title3,
                    R.string.onboarding_intro3
                )
            ))
        }
    }

    private fun switchAds() {
        viewBinding.navigationLayout.visible()

        if(onBoardingAdapter.itemCount == 4){
            when (currentPosition) {
                0 -> {
                    intNativeToView(NativeAdmobUtils.onboardNativeAdmob1)
                }

                1 -> {
                    intNativeToView(NativeAdmobUtils.onboardNativeAdmob2)
                }

                2 -> {
                    viewBinding.navigationLayout.gone()
                }

                3 -> {
                    intNativeToView(NativeAdmobUtils.onboardNativeAdmob3)
                }

                else -> {
                    intNativeToView(NativeAdmobUtils.onboardNativeAdmob1)
                }
            }
        }else{
            when (currentPosition) {
                0 -> {
                    intNativeToView(NativeAdmobUtils.onboardNativeAdmob1)
                }

                1 -> {
                    intNativeToView(NativeAdmobUtils.onboardNativeAdmob2)
                }

                2 -> {
                    intNativeToView(NativeAdmobUtils.onboardNativeAdmob3)
                }

                else -> {
                    intNativeToView(NativeAdmobUtils.onboardNativeAdmob1)
                }
            }
        }
    }

    private fun intNativeToView(nativeAdmob: NativeAdmob?) {
        currentNativeAdmob = nativeAdmob;
        nativeAdmob?.showNative(viewBinding.flAdplaceholder, null)
    }

    override fun initObserver() {
        super.initObserver()
        viewBinding.flAdplaceholder.visibility = View.GONE
        NativeAdmobUtils.onboardNativeAdmob1?.run {
            nativeAdLive?.observe(this@OnBoardingActivity){
                if(available() && spManager.getBoolean(NameRemoteAdmob.NATIVE_ONBOARD, true)){
                    if(currentPosition == 0){
                        viewBinding.flAdplaceholder.visibility = View.VISIBLE
                        intNativeToView(this)
                    }
                }
            }
        }
        NativeAdmobUtils.onboardNativeAdmob2?.run {
            nativeAdLive?.observe(this@OnBoardingActivity){
                if(available() && spManager.getBoolean(NameRemoteAdmob.NATIVE_ONBOARD, true)){
                    if(currentPosition == 1){
                        viewBinding.flAdplaceholder.visibility = View.VISIBLE
                        intNativeToView(this)
                    }
                }
            }
        }
        NativeAdmobUtils.onboardNativeAdmob3?.run {
            nativeAdLive?.observe(this@OnBoardingActivity){
                if(available() && spManager.getBoolean(NameRemoteAdmob.NATIVE_ONBOARD, true)){
                    if(currentPosition == 2){
                        viewBinding.flAdplaceholder.visibility = View.VISIBLE
                        intNativeToView(this)
                    }
                }
            }
        }

        NativeAdmobUtils.onboardFullNativeAdmob?.run {
            nativeAdLive.observe(this@OnBoardingActivity){
                if(available()){
                    addAdsToOnboard(this)
                }
            }
        }
    }

    private fun addAdsToOnboard(it: NativeAdmob?) {
        if(spManager.getBoolean(NameRemoteAdmob.NATIVE_FULL_SCREEN, true)){
            val adsOnboard = OnBoarding(
                OnBoarding.FULL_NATIVE_FLAG,
                OnBoarding.FULL_NATIVE_FLAG,
                OnBoarding.FULL_NATIVE_FLAG,
                it
            )

            onBoardingAdapter.getListData().add(2, adsOnboard)
            onBoardingAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        if(NetworkUtil.isOnline){
            currentNativeAdmob?.reLoad()
        }
        super.onResume()
    }
}
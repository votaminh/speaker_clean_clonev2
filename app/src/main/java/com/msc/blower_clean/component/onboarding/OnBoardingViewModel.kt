package com.msc.blower_clean.component.onboarding

import androidx.lifecycle.ViewModel
import com.msc.blower_clean.R


class OnBoardingViewModel : ViewModel() {
    val listOnBoarding = listOf(
        OnBoarding(
            R.drawable.ic_onboarding1,
            R.string.onboarding_title1,
            R.string.onboarding_intro1
        ),
        OnBoarding(
            R.drawable.ic_onboarding2,
            R.string.onboarding_title1,
            R.string.onboarding_intro2
        ),
        OnBoarding(
            R.drawable.ic_onboarding3,
            R.string.onboarding_title1,
            R.string.onboarding_intro3
        )
    )
}
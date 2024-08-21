package com.msc.blower_clean.component.vibrate

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import com.msc.blower_clean.R
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.test_speaker.TestSpeakerActivity
import com.msc.blower_clean.databinding.ActivityVibrateClone2Binding
import com.msc.speaker_cleaner.component.cleanervibrate.StateVibrateClone2
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VibrateCleanActivity : BaseActivity<ActivityVibrateClone2Binding>() {
    val viewModel: VibrateViewModelClone2 by viewModels()
    private var isUseFront = true

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, VibrateCleanActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityVibrateClone2Binding {
        return ActivityVibrateClone2Binding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        lifecycle.addObserver(viewModel)

        viewBinding.apply {
            imvPlay.setOnClickListener { viewModel.start() }
            tvNormal.setOnClickListener {
                viewModel.setInensity(IntensityVibrate.NORMAL_CLONE2)
                isUseFront = true
                updateUiSpeaker()
            }
            tvStrong.setOnClickListener {
                viewModel.setInensity(IntensityVibrate.STRONG_CLONE2)
                isUseFront = false
                updateUiSpeaker()
            }

            imvBack.setOnClickListener {
                finish()
            }
        }
    }

    override fun initObserver() {
        super.initObserver()

        viewModel.run {
            stateLive.observe(this@VibrateCleanActivity) {
                when (it) {
                    StateVibrateClone2.PLAYING -> {
                        viewBinding.imvPlay.setImageResource(R.drawable.ic_pause)
                    }

                    StateVibrateClone2.STOP -> {
                        viewBinding.imvPlay.setImageResource(R.drawable.ic_play)
                    }
                }
            }

            percentLive.observe(this@VibrateCleanActivity) {
                viewBinding.tvPercent.text = "$it %"
                if (it == 100) {
                    TestSpeakerActivity.start(this@VibrateCleanActivity)
                }
            }

            intensityLive.observe(this@VibrateCleanActivity) {
                when (it) {
                    IntensityVibrate.NORMAL_CLONE2 -> {
                    }

                    IntensityVibrate.STRONG_CLONE2 -> {
                    }
                }
            }
        }
    }

    private fun updateUiSpeaker() {
        viewBinding.run {
            if(isUseFront){
                tvNormal.animate().alpha(1f).setDuration(0).start()
                tvStrong.animate().alpha(0.3f).setDuration(0).start()
            }else{
                tvNormal.animate().alpha(0.3f).setDuration(0).start()
                tvStrong.animate().alpha(1f).setDuration(0).start()
            }
        }
    }
}
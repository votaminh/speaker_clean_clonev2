package com.msc.demo_mvvm.component.vibrate

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.msc.demo_mvvm.R
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.component.test_speaker.TestSpeakerActivity
import com.msc.demo_mvvm.databinding.ActivityVibrateClone2Binding
import com.msc.speaker_cleaner.component.cleanervibrate.IntensityVibrate
import com.msc.speaker_cleaner.component.cleanervibrate.StateVibrate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VibrateCleanActivity : BaseActivity<ActivityVibrateClone2Binding>() {
    val viewModel: VibrateViewModel by viewModels()
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
            tvNormal.setOnClickListener { viewModel.setInensity(IntensityVibrate.NORMAL) }
            tvStrong.setOnClickListener { viewModel.setInensity(IntensityVibrate.STRONG) }
        }
    }

    override fun initObserver() {
        super.initObserver()

        viewModel.run {
            stateLive.observe(this@VibrateCleanActivity) {
                when (it) {
                    StateVibrate.PLAYING -> {
                        viewBinding.imvPlay.setImageResource(R.drawable.ic_pause)
                    }

                    StateVibrate.STOP -> {
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
                    IntensityVibrate.NORMAL -> {
//                        viewBinding.btnNormal.isSelected = true
//                        viewBinding.btnStrong.isSelected = false
                    }

                    IntensityVibrate.STRONG -> {
//                        viewBinding.btnNormal.isSelected = false
//                        viewBinding.btnStrong.isSelected = true
                    }
                }
            }
        }
    }
}
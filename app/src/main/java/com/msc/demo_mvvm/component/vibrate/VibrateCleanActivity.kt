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
                viewModel.setInensity(IntensityVibrate.NORMAL)
                isUseFront = true
                updateUiSpeaker()
            }
            tvStrong.setOnClickListener {
                viewModel.setInensity(IntensityVibrate.STRONG)
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
                    }

                    IntensityVibrate.STRONG -> {
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
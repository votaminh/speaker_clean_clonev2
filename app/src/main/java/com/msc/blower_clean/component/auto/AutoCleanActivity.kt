package com.msc.blower_clean.component.auto

import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import com.msc.blower_clean.R
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.test_speaker.TestSpeakerActivity
import com.msc.blower_clean.databinding.ActivityAutoClone2Binding
import com.msc.blower_clean.utils.ViewEx.gone
import com.msc.blower_clean.utils.ViewEx.visible
import com.msc.speaker_cleaner.domain.layer.SourceAudio
import com.msc.speaker_cleaner.domain.layer.StateAudio
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AutoCleanActivity : BaseActivity<ActivityAutoClone2Binding>() {
    private val viewModel: AutoViewModel by viewModels()
    private var isUseFront = true

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, AutoCleanActivity::class.java))
        }

    }

    override fun provideViewBinding(): ActivityAutoClone2Binding {
        return ActivityAutoClone2Binding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()

        viewBinding.run {
            viewBinding.run {
                imvBack.setOnClickListener {
                    finish()
                }

                imvPlay.setOnClickListener {
                    viewModel.start()
                }

                tvFront.setOnClickListener {
                    viewModel.useSpeaker()
                    isUseFront = true
                    updateUiSpeaker()
                }

                tvEar.setOnClickListener {
                    viewModel.useEarpiece()
                    isUseFront = false
                    updateUiSpeaker()
                }

            }
        }
    }

    private fun updateUiSpeaker() {
        viewBinding.run {
            if(isUseFront){
                tvFront.animate().alpha(1f).setDuration(0).start()
                tvEar.animate().alpha(0.3f).setDuration(0).start()
            }else{
                tvEar.animate().alpha(1f).setDuration(0).start()
                tvFront.animate().alpha(0.3f).setDuration(0).start()
            }
        }
    }

    override fun initObserver() {
        super.initObserver()

        viewModel.run {
            stateAudio.observe(this@AutoCleanActivity) {
                when (it) {
                    StateAudio.PLAYING -> {
                        viewBinding.imvPlay.setImageResource(R.drawable.ic_pause)
                        viewBinding.tvContAuto.visible()
                        viewBinding.lnBtnFrontEar.gone()
                    }

                    StateAudio.STOP -> {
                        viewBinding.imvPlay.setImageResource(R.drawable.ic_play)
                        viewBinding.tvContAuto.gone()
                        viewBinding.lnBtnFrontEar.visible()
                    }
                }
            }

            percentCleaner.observe(this@AutoCleanActivity) {
                viewBinding.tvPercent.text = "$it %"
                viewBinding.arcView.setProgress(it)

                if (it == 100) {
                    TestSpeakerActivity.start(this@AutoCleanActivity)
                }
            }

            sourceAudio.observe(this@AutoCleanActivity) {
                when (it) {
                    SourceAudio.FRONT -> {
                        viewBinding.run {
                            isUseFront = true
                            updateUiSpeaker()
                        }
                    }

                    SourceAudio.EAR -> {
                        viewBinding.run {
                            isUseFront = false
                            updateUiSpeaker()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        kotlin.runCatching {
            viewModel.stopAudio()
        }
    }
}
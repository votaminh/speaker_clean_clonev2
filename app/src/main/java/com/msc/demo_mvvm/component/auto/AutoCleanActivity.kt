package com.msc.demo_mvvm.component.auto

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.msc.demo_mvvm.R
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.component.test_speaker.TestSpeakerActivity
import com.msc.demo_mvvm.databinding.ActivityAutoClone2Binding
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
//                        viewBinding.tvContAuto.visibility = View.VISIBLE
//                        viewBinding.lnBtnFrontEar.visibility = View.GONE
                    }

                    StateAudio.STOP -> {
                        viewBinding.imvPlay.setImageResource(R.drawable.ic_play)
//                        viewBinding.tvContAuto.visibility = View.GONE
//                        viewBinding.lnBtnFrontEar.visibility = View.VISIBLE
                    }
                }
            }

            percentCleaner.observe(this@AutoCleanActivity) {
                viewBinding.tvPercent.text = "$it %"
//                viewBinding.arcView.setProgress(it.toFloat())

//                if (it == 100) {
//                    activity?.let {
//                        (activity as MainActivity).showInter{
//                            TestSpeakerActivity.start(requireActivity())
//                        }
//                    }
//                }
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
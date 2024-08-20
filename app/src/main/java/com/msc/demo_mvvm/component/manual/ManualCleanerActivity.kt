package com.msc.demo_mvvm.component.manual

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.msc.demo_mvvm.R
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.component.test_speaker.TestSpeakerActivity
import com.msc.demo_mvvm.databinding.ActivityManualCleanerClone2Binding
import com.msc.speaker_cleaner.domain.layer.SourceAudio
import com.msc.speaker_cleaner.domain.layer.StateAudio
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManualCleanerActivity : BaseActivity<ActivityManualCleanerClone2Binding>() {
    private val viewModel: ManualViewModel by viewModels()
    private var canOpenTest = false
    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, ManualCleanerActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityManualCleanerClone2Binding {
        return ActivityManualCleanerClone2Binding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        lifecycle.addObserver(viewModel)

        viewBinding.apply {
            imvPlay.setOnClickListener {
                viewModel.start()
            }

//            sbVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                    viewModel.setFrequency(
//                        p1.range(
//                            AutoThreadAudio.MIN_FREQUENCY.toFloat(),
//                            AutoThreadAudio.MAX_FREQUENCY.toFloat()
//                        )
//                    )
//                }
//
//                override fun onStartTrackingTouch(p0: SeekBar?) {
//                }
//
//                override fun onStopTrackingTouch(p0: SeekBar?) {
//                }
//
//            })

            tvFront.setOnClickListener {
                viewModel.setFront()
            }
            tvEar.setOnClickListener {
                viewModel.setEar()
            }
        }
    }

    override fun initObserver() {
        super.initObserver()

        viewModel.run {
            stateAudio.observe(this@ManualCleanerActivity) {
                when (it) {
                    StateAudio.PLAYING -> {
                        viewBinding.imvPlay.setImageResource(R.drawable.ic_pause)
//                        viewBinding.tvIntro.text =
//                            getString(R.string.run_23_message)
//                        viewBinding.lnBtnFontEar.visibility = View.GONE
                        canOpenTest = true
                    }

                    StateAudio.STOP -> {
                        viewBinding.imvPlay.setImageResource(R.drawable.ic_play)
//                        viewBinding.tvIntro.text = getString(R.string.speaker)
//                        viewBinding.lnBtnFontEar.visibility = View.VISIBLE

                        if(canOpenTest){
                            canOpenTest = false
                            TestSpeakerActivity.start(this@ManualCleanerActivity)
                        }
                    }
                }
            }

            frequencyAudioLive.observe(this@ManualCleanerActivity) {
//                viewBinding.tvHZ.text = "$it HZ"
            }

            sourceAudioLive.observe(this@ManualCleanerActivity) {
                when (it) {
                    SourceAudio.FRONT -> {
                        viewBinding.run {
//                            tvFront.isSelected = true
//                            tvEar.isSelected = false
                        }
                    }

                    SourceAudio.EAR -> {
                        viewBinding.run {
//                            btnFront.isSelected = false
//                            btnEar.isSelected = true
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        kotlin.runCatching {
            viewModel.cancelAudio()
            canOpenTest = false
        }
    }
}
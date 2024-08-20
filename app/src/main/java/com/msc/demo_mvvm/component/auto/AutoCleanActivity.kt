package com.msc.demo_mvvm.component.auto

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.msc.demo_mvvm.R
import com.msc.demo_mvvm.base.activity.BaseActivity
import com.msc.demo_mvvm.component.test_speaker.TestSpeakerActivity
import com.msc.demo_mvvm.databinding.ActivityAutoClone2Binding

class AutoCleanActivity : BaseActivity<ActivityAutoClone2Binding>() {

    private var aniSpeaker : ValueAnimator? = null

    private var isUseFront = true
    private var isPlaying = false

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
            imvPlay.setOnClickListener {
                if(isPlaying){
                    isPlaying = false
                    pauseSpeaker()
                }else{
                    isPlaying = true
                    playSpeaker()
                }
            }

            tvFront.setOnClickListener {
                isUseFront = true
                updateUiSpeaker()
            }

            tvEar.setOnClickListener {
                isUseFront = false
                updateUiSpeaker()
            }
        }
    }

    private fun pauseSpeaker() {
        viewBinding.run {
            imvPlay.setImageResource(R.drawable.ic_play)
        }

        aniSpeaker?.run {
            pause()
        }

        TestSpeakerActivity.start(this@AutoCleanActivity)
    }

    private fun playSpeaker() {

        viewBinding.run {
            imvPlay.setImageResource(R.drawable.ic_pause)
        }

        aniSpeaker = ValueAnimator.ofInt(0,100)
        aniSpeaker?.run {
            duration = 5000
            addUpdateListener { animation ->
                val progress = animation.animatedValue as Int
                viewBinding.tvPercent.text = "$progress%"
            }
            start()
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
}
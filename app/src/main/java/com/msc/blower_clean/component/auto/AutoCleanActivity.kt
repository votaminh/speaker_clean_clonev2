package com.msc.blower_clean.component.auto

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import com.msc.blower_clean.R
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.auto.auto.AutoThreadAudio
import com.msc.blower_clean.component.auto.auto.rv2
import com.msc.blower_clean.component.test_speaker.TestSpeakerActivity
import com.msc.blower_clean.databinding.ActivityAutoClone2Binding
import com.msc.blower_clean.utils.InterNativeUtils
import com.msc.blower_clean.utils.RewardUtils
import com.msc.blower_clean.utils.ViewEx.gone
import com.msc.blower_clean.utils.ViewEx.visible
import com.msc.speaker_cleaner.domain.layer.SourceAudio
import com.msc.speaker_cleaner.domain.layer.StateAudio
import dagger.hilt.android.AndroidEntryPoint

class AutoCleanActivity : BaseActivity<ActivityAutoClone2Binding>() {
    private var isUseFront = true

    private var va: ValueAnimator? = null
    private var autoThreadAudio: AutoThreadAudio? = null
    val stateAudio = MutableLiveData<StateAudio>()
    val sourceAudio = MutableLiveData(SourceAudio.FRONT)
    val percentCleaner = MutableLiveData<Int>()
    var timeReward = 0L
    private val rv2: rv2 = rv2()

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
                    InterNativeUtils.showInterAction(this@AutoCleanActivity){
                        finish()
                    }
                }

                imvPlay.setOnClickListener {
                    if(autoThreadAudio != null){
                        start()
                    }else{
                        RewardUtils.showRewardFeature(this@AutoCleanActivity, nextAction = {
                            timeReward = System.currentTimeMillis()
                            start()
                        })
                    }
                }

                tvFront.setOnClickListener {
                    useSpeaker()
                    isUseFront = true
                    updateUiSpeaker()
                }

                tvEar.setOnClickListener {
                    useEarpiece()
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

        run {
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

    fun start() {
        if(autoThreadAudio == null){
            autoThreadAudio =
                AutoThreadAudio(
                    this@AutoCleanActivity,
                    rv2
                )

            stateAudio.postValue(StateAudio.PLAYING)

            rv2.a = (170.0) * 5.0
            rv2.a(100)

            autoThreadAudio?.setVolume(0f);
            autoThreadAudio?.start()

            va = ValueAnimator.ofFloat(0f, 1f)
            va?.run {
                setDuration(10000)
                addUpdateListener((ValueAnimator.AnimatorUpdateListener { animation: ValueAnimator ->
                    val volume = animation.animatedValue as Float
                    autoThreadAudio?.setVolume(volume)
                    val percent = (volume * 100).toInt()
                    percentCleaner.postValue(percent)
                }))
                addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {
                    }

                    override fun onAnimationEnd(p0: Animator) {
                        autoThreadAudio?.stopAudio()
                        va?.cancel()
                    }

                    override fun onAnimationCancel(p0: Animator) {
                    }

                    override fun onAnimationRepeat(p0: Animator) {
                    }
                })
                start()
            }
        }else{
            stopAudio()
        }
    }

    fun stopAudio() {
        stateAudio.postValue(StateAudio.STOP)
        percentCleaner.postValue(0)
        autoThreadAudio?.stopAudio()
        autoThreadAudio = null
        va?.cancel()
    }

    fun useEarpiece() {
        autoThreadAudio?.useEarpiece()
        sourceAudio.postValue(SourceAudio.EAR)
    }

    fun useSpeaker() {
        autoThreadAudio?.useSpeaker()
        sourceAudio.postValue(SourceAudio.FRONT)
    }

    override fun onResume() {
        super.onResume()
        if(System.currentTimeMillis() - timeReward < 1000){
            return
        }
        kotlin.runCatching {
            stopAudio()
        }
    }

    override fun onPause() {
        super.onPause()
        if(System.currentTimeMillis() - timeReward < 1000){
            return
        }
        kotlin.runCatching {
            stopAudio()
        }
    }
}
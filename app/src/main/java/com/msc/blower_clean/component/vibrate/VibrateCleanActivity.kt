package com.msc.blower_clean.component.vibrate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.msc.blower_clean.R
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.test_speaker.TestSpeakerActivity
import com.msc.blower_clean.databinding.ActivityVibrateClone2Binding
import com.msc.blower_clean.utils.ViewEx.invisible
import com.msc.blower_clean.utils.ViewEx.visible
import com.msc.speaker_cleaner.component.cleanervibrate.StateVibrateClone2
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VibrateCleanActivity : BaseActivity<ActivityVibrateClone2Binding>() {
    private var isUseFront = true

    var g = 50000L
    var h = 500L
    var i: Int = 150

    val stateLive = MutableLiveData(StateVibrateClone2.STOP)
    val intensityLive = MutableLiveData(IntensityVibrate.NORMAL_CLONE2)
    val percentLive = MutableLiveData(0)

    private var countDownTimer : CountDownTimer? = null


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

        viewBinding.apply {
            imvPlay.setOnClickListener { start() }
            tvNormal.setOnClickListener {
                setInensity(IntensityVibrate.NORMAL_CLONE2)
                isUseFront = true
                updateUiSpeaker()
            }
            tvStrong.setOnClickListener {
                setInensity(IntensityVibrate.STRONG_CLONE2)
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

        run {
            stateLive.observe(this@VibrateCleanActivity) {
                when (it) {
                    StateVibrateClone2.PLAYING -> {
                        viewBinding.imvPlay.setImageResource(R.drawable.ic_pause)
                        viewBinding.lnBtnFontEar.invisible()
                        viewBinding.tvIntro.visible()
                    }

                    StateVibrateClone2.STOP -> {
                        viewBinding.imvPlay.setImageResource(R.drawable.ic_play)
                        viewBinding.lnBtnFontEar.visible()
                        viewBinding.tvIntro.invisible()
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

    fun start(){

        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        if(countDownTimer == null){
            countDownTimer = object : CountDownTimer(g, h) {
                override fun onTick(p0: Long) {

                    val progress = ((g - p0) / g.toFloat()) * 100
                    stateLive.postValue(StateVibrateClone2.PLAYING)
                    percentLive.postValue(progress.toInt())

                    if (Build.VERSION.SDK_INT >= 26) {
                        vibrator?.vibrate(
                            VibrationEffect.createOneShot(
                                i.toLong(),
                                -1
                            )
                        )
                    } else {
                        vibrator?.vibrate(i.toLong())
                    }
                }
                override fun onFinish() {
                    stateLive.postValue(StateVibrateClone2.STOP)
                    percentLive.postValue(100)
                }
            }

            countDownTimer?.start()
            stateLive.postValue(StateVibrateClone2.PLAYING)
        }else{
            cancelVibrate()
        }
    }

    fun cancelVibrate() {
        countDownTimer?.run {
            cancel()
            countDownTimer = null
            stateLive.postValue(StateVibrateClone2.STOP)
            percentLive.postValue(0)
        }
    }

    fun setInensity(intensity: IntensityVibrate) {
        intensityLive.postValue(intensity)
        when(intensity){
            IntensityVibrate.NORMAL_CLONE2 -> {
                i = 150
            }
            IntensityVibrate.STRONG_CLONE2 -> {
                i = 500
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onActivityPause() {
        cancelVibrate()
    }

    override fun onPause() {
        super.onPause()
        kotlin.runCatching {
            cancelVibrate()
        }
    }
}
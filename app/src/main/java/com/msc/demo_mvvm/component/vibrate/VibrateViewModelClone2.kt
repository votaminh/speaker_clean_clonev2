package com.msc.demo_mvvm.component.vibrate

import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ViewModel
import com.msc.speaker_cleaner.component.cleanervibrate.IntensityVibrate
import com.msc.speaker_cleaner.component.cleanervibrate.StateVibrateClone2
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class VibrateViewModelClone2 @Inject constructor(@ApplicationContext val context: Context) : ViewModel(), LifecycleObserver {
    var g = 50000L
    var h = 500L
    var i: Int = 150

    val stateLive = MutableLiveData(StateVibrateClone2.STOP)
    val intensityLive = MutableLiveData(IntensityVibrate.NORMAL_CLONE2)
    val percentLive = MutableLiveData(0)

    private var countDownTimer : CountDownTimer? = null
    fun start(){

        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

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
}
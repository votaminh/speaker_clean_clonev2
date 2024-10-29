package com.msc.blower_clean.component.blower

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.auto.auto.AutoThreadAudio
import com.msc.blower_clean.component.auto.auto.rv2
import com.msc.blower_clean.databinding.ActivityBlowerClone2Binding
import com.msc.blower_clean.utils.AppEx.range
import com.msc.blower_clean.utils.InterNativeUtils
import com.msc.blower_clean.utils.RewardUtils

class BlowerActivity : BaseActivity<ActivityBlowerClone2Binding>() {

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, BlowerActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityBlowerClone2Binding {
        return ActivityBlowerClone2Binding.inflate(layoutInflater)
    }

    private var autoThreadAudio: AutoThreadAudio? = null
    private var latestRequestRotation = 0L
    private var rotationAnimator: ObjectAnimator? = null
    private val MIN_PERCENT = 0.05f
    private val MAX_PERCENT = 0.95f

    private var isRunning = false
    private var currentLevelPercent = 0.5f;
    private var latestClick = 0L
    private var prePecent = 0f;

    override fun initViews() {
        super.initViews()
        viewBinding.run {
            imvTurn.setOnClickListener {
                if(viewBinding.imvTurn.alpha != 1f){
                    return@setOnClickListener
                }

                if(isRunning){
                    process()
                }else{
                    RewardUtils.showRewardFeature(this@BlowerActivity){
                        process()
                    }
                }
            }

            booster.setOnClickListener {
                if(!isRunning || currentLevelPercent > MAX_PERCENT || viewBinding.booster.alpha != 1f){
                    return@setOnClickListener
                }
                val currentTime = System.currentTimeMillis()
                if(currentTime - latestClick > 200){
                    currentLevelPercent += 0.05f
                    animationIndicatorTo(currentLevelPercent, false)
                    latestClick = System.currentTimeMillis()
                }

                setPercentAudio(currentLevelPercent * 100)
            }

            lower.setOnClickListener {
                if(!isRunning || currentLevelPercent < MIN_PERCENT || viewBinding.lower.alpha != 1f){
                    return@setOnClickListener
                }
                val currentTime = System.currentTimeMillis()
                if(currentTime - latestClick > 200){
                    currentLevelPercent -= 0.05f
                    animationIndicatorTo(currentLevelPercent, false)
                    latestClick = System.currentTimeMillis()
                    setPercentAudio(currentLevelPercent * 100)
                }
            }

            btnBack.setOnClickListener {
                InterNativeUtils.showInterAction(this@BlowerActivity){
                    finish()
                }
            }
        }
    }

    private fun process() {
        viewBinding.run {
            if(isRunning){
                currentLevelPercent = 0.05f
                booster.animate().alpha(0.3f).start()
                lower.animate().alpha(0.3f).start()
            }else{
                currentLevelPercent = 0.5f
                startAudio()
                setPercentAudio(currentLevelPercent * 100)
            }
            isRunning = !isRunning
            animationIndicatorTo(currentLevelPercent, true)

            imvTurn.animate().alpha(0.3f).start()
            android.os.Handler(Looper.getMainLooper()).postDelayed({
                imvTurn.animate().alpha(1f).start()
                if(isRunning){
                    booster.animate().alpha(1f).start()
                    lower.animate().alpha(1f).start()
                }
            }, 3000)
        }
    }

    private fun setPercentAudio(fl: Float) {
        val fre = range(fl.toInt(), 50f, 80f)
        autoThreadAudio?.setFrequency(fre.toDouble())
    }

    private fun startAudio() {
        autoThreadAudio?.stopAudio()
        val rv2 = rv2()
        autoThreadAudio =
            AutoThreadAudio(
                this@BlowerActivity,
                rv2
            )

        rv2.a = (170.0) * 5.0
        rv2.a(100)

        autoThreadAudio?.run{
            setVolume(100f)
            useSpeaker()
        }
        autoThreadAudio?.start()
    }

    private fun animationIndicatorTo(percent: Float, animationIndicator : Boolean) {
        if(prePecent == 0f){
            prePecent = percent
        }

        if(isRunning){
            rotateViewContinuously(viewBinding.imvPropeller, (percent * 100).toInt())
        }else{
            val v = ValueAnimator.ofFloat(prePecent, MIN_PERCENT)
            v.setDuration(3000)
            v.addUpdateListener {
                val percent = it.animatedValue as Float
                Log.i("gdsgxx", "animationIndicatorTo: " + percent)
                setPercentAudio(percent * 100)
                rotateViewContinuously(viewBinding.imvPropeller, (percent * 100).toInt())
            }
            v.addListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                    autoThreadAudio?.stopAudio()
                    rotationAnimator?.cancel()
                }

                override fun onAnimationCancel(p0: Animator) {
                }

                override fun onAnimationRepeat(p0: Animator) {
                }

            })
            v.start()
        }

        prePecent = percent

        var mPercent = percent
        if(mPercent < MIN_PERCENT){
            mPercent = MIN_PERCENT
        }

        if(mPercent > MAX_PERCENT){
            mPercent = MAX_PERCENT
        }


        viewBinding.run {
            val maxWidth = imvLevelBlower.width
            val targetWidth = (maxWidth * mPercent) - (imvIndicatorBar.width / 2)

            if(animationIndicator){
                imvIndicatorBar.animate().translationX(targetWidth).setDuration(3000).start()
            }else{
                imvIndicatorBar.animate().translationX(targetWidth).setDuration(0).start()
            }
        }
    }

    private fun rotateViewContinuously(view: View, speed: Int) {

        if(speed == (MIN_PERCENT * 100).toInt()){
            rotationAnimator?.cancel()
            return
        }

        if(System.currentTimeMillis() - latestRequestRotation < 1000){
            return
        }

        latestRequestRotation = System.currentTimeMillis()

        rotationAnimator?.cancel()
        rotationAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        var d = (101 - speed) * 5L

        if(d < 100){
            d = 100
        }

        rotationAnimator?.run {
            interpolator = LinearInterpolator()
            duration = d
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            start()
        }
    }

    override fun onPause() {
        autoThreadAudio?.stopAudio()
        isRunning = false
        viewBinding.imvTurn.animate().alpha(1f).start()
        viewBinding.booster.animate().alpha(0.3f).start()
        viewBinding.lower.animate().alpha(0.3f).start()
        rotationAnimator?.cancel()
        val maxWidth = viewBinding.imvLevelBlower.width
        val targetWidth = (maxWidth * MIN_PERCENT) - (viewBinding.imvIndicatorBar.width / 2)
        viewBinding.imvIndicatorBar.animate().translationX(targetWidth).setDuration(0).start()
        super.onPause()
    }
}
package com.msc.blower_clean.component.manual

import android.app.Activity
import android.content.Intent
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.msc.blower_clean.R
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.component.auto.auto.AutoThreadAudio
import com.msc.blower_clean.component.auto.auto.rv2
import com.msc.blower_clean.component.test_speaker.TestSpeakerActivity
import com.msc.blower_clean.databinding.ActivityManualCleanerClone2Binding
import com.msc.blower_clean.utils.AppEx.range
import com.msc.blower_clean.utils.ViewEx.gone
import com.msc.blower_clean.utils.ViewEx.invisible
import com.msc.blower_clean.utils.ViewEx.visible
import com.msc.speaker_cleaner.domain.layer.SourceAudio
import com.msc.speaker_cleaner.domain.layer.StateAudio
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ManualCleanerActivity : BaseActivity<ActivityManualCleanerClone2Binding>() {
    private var canOpenTest = false
    private var isUseFront = true

    private var autoThreadAudio: AutoThreadAudio? = null
    private val rv2: rv2 =
        rv2()

    val stateAudio = MutableLiveData<StateAudio>()
    val frequencyAudioLive = MutableLiveData<Int>()
    val sourceAudioLive = MutableLiveData(SourceAudio.FRONT)

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

        viewBinding.apply {

            imvBack.setOnClickListener {
                finish()
            }

            imvPlay.setOnClickListener {
                start()
            }

            sbVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    setFrequency(
                        range(
                            p1,
                            AutoThreadAudio.MIN_FREQUENCY.toFloat(),
                            AutoThreadAudio.MAX_FREQUENCY.toFloat()
                        )
                    )
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {
                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                }

            })

            setFrequency(
                range(
                    50,
                    AutoThreadAudio.MIN_FREQUENCY.toFloat(),
                    AutoThreadAudio.MAX_FREQUENCY.toFloat()
                )
            )

            tvFront.setOnClickListener {
                setFront()
                isUseFront = true
                updateUiSpeaker()
            }
            tvEar.setOnClickListener {
                setEar()
                isUseFront = false
                updateUiSpeaker()
            }
        }
    }

    override fun initObserver() {
        super.initObserver()

        run {
            stateAudio.observe(this@ManualCleanerActivity) {
                when (it) {
                    StateAudio.PLAYING -> {
                        viewBinding.imvPlay.setImageResource(R.drawable.ic_pause)
                        viewBinding.lnBtnFontEar.invisible()
                        viewBinding.tvIntro.visible()
                        canOpenTest = true
                    }

                    StateAudio.STOP -> {
                        viewBinding.imvPlay.setImageResource(R.drawable.ic_play)
                        viewBinding.lnBtnFontEar.visible()
                        viewBinding.tvIntro.invisible()

                        if(canOpenTest){
                            canOpenTest = false
                            TestSpeakerActivity.start(this@ManualCleanerActivity)
                        }
                    }
                }
            }

            frequencyAudioLive.observe(this@ManualCleanerActivity) {
                viewBinding.tvHZ.text = "$it HZ"
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

    override fun onResume() {
        super.onResume()

        kotlin.runCatching {
            cancelAudio()
            canOpenTest = false
        }
    }

    fun start(){
        if(autoThreadAudio == null){
            autoThreadAudio =
                AutoThreadAudio(
                    this,
                    rv2
                )

            rv2.a = (170.0) * 5.0
            rv2.a(100)

            autoThreadAudio?.run{
                setVolume(100f)
                val frequency = frequencyAudioLive.value
                frequency?.let {
                    setFrequency(it.toDouble())
                }
                if(sourceAudioLive.value == SourceAudio.EAR){
                    useEarpiece()
                }else{
                    useSpeaker()
                }
                start()
            }
            stateAudio.postValue(StateAudio.PLAYING)
        }else{
            cancelAudio()
        }
    }

    fun cancelAudio() {
        autoThreadAudio?.stopAudio()
        autoThreadAudio = null
        stateAudio.postValue(StateAudio.STOP)
    }

    fun setFrequency(frequency: Float) {
        autoThreadAudio?.setFrequency(frequency.toDouble())
        frequencyAudioLive.postValue(frequency.toInt())
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onActivityPause() {
        cancelAudio()
    }

    fun setFront() {
        sourceAudioLive.postValue(SourceAudio.FRONT)
        autoThreadAudio?.useSpeaker()
    }

    fun setEar(){
        sourceAudioLive.postValue(SourceAudio.EAR)
        autoThreadAudio?.useEarpiece()
    }

    override fun onPause() {
        super.onPause()
        kotlin.runCatching {
            autoThreadAudio?.stopAudio()
        }
    }
}
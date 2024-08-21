package com.msc.blower_clean.component.test_speaker

import android.animation.ObjectAnimator
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.media.MediaPlayer
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.SeekBar
import com.msc.blower_clean.R
import com.msc.blower_clean.base.activity.BaseActivity
import com.msc.blower_clean.databinding.ActivityTestSpeakerClone2Binding

class TestSpeakerActivity : BaseActivity<ActivityTestSpeakerClone2Binding>() {
    private lateinit var audioManager: AudioManager
    private var mediaPlayer: MediaPlayer? = null
    private var rotationAnimator: ObjectAnimator? = null

    private var volumeReceiver : BroadcastReceiver? = null
    var CURRENT_VOLUME = 0

    companion object {
        fun start(activity : Activity){
            activity.startActivity(Intent(activity, TestSpeakerActivity::class.java))
        }
    }

    override fun provideViewBinding(): ActivityTestSpeakerClone2Binding {
        return ActivityTestSpeakerClone2Binding.inflate(layoutInflater)
    }

    override fun initViews() {
        super.initViews()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        viewBinding.apply {
            sbVolume.max = maxVolume
            sbVolume.progress = currentVolume
            CURRENT_VOLUME = currentVolume

            imvBack.setOnClickListener { onBack() }

            sbVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
                        CURRENT_VOLUME = progress
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })

            rotationImage()

            btnPlayAndPause.setOnClickListener {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(this@TestSpeakerActivity, R.raw.sound)
                    mediaPlayer?.start()
                    rotationAnimator?.start()
                    btnPlayAndPause.setImageResource(R.drawable.ic_pause)
                    tvContTest.text = getString(R.string.if_you_didn_mess)
                } else {
                    if (mediaPlayer?.isPlaying == true) {
                        pause()
                    } else {
                        play()
                    }
                }
            }
        }
    }

    private fun play() {
        mediaPlayer?.start()
        rotationAnimator?.resume()
        viewBinding.btnPlayAndPause.setImageResource(R.drawable.ic_pause)
        viewBinding.tvContTest.text = getString(R.string.if_you_didn_mess)
    }

    private fun pause() {
        mediaPlayer?.pause()
        rotationAnimator?.pause()
        viewBinding.btnPlayAndPause.setImageResource(R.drawable.ic_play)
        viewBinding.tvContTest.text = getString(R.string.txt_test_speaker_message)
    }

    private fun rotationImage() {
        val rotatingImageView: ImageView = viewBinding.ivPlate

        rotationAnimator = ObjectAnimator.ofFloat(rotatingImageView, "rotation", 0f, 360f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.RESTART
            interpolator = LinearInterpolator()
        }

    }

    private fun registerVolumeReceiver() {
        volumeReceiver = object : BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                updateSbVolume()
            }
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(volumeReceiver, intentFilter)
    }

    private fun updateSbVolume() {
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        viewBinding.sbVolume.progress = currentVolume
    }

    override fun onResume() {
        super.onResume()

        kotlin.runCatching {
            registerVolumeReceiver()
            updateSbVolume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onPause() {
        super.onPause()
        pause()
        unregisterReceiver(volumeReceiver)
    }
}
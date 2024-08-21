package com.msc.blower_clean.component.auto.auto;

public final class rv2 {
    public double a = 440.0d;
    public AutoThreadAudio b;
    public boolean c;

    public final void a(int i) {
        float f = ((float) i) / 100.0f;
        AutoThreadAudio ThreadAudio = this.b;
        if (ThreadAudio != null) {
            ThreadAudio.audioTrack.setVolume(f);
        }
    }
}

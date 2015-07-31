package net.exkazuu.mimicdance.controller;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

public class PwmMotorController {
    private static final int channelConfig = AudioFormat.CHANNEL_OUT_STEREO;
    private static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    private short[] buffer;
    private int sampleRate;

    private int originalCycleLength;
    private int originalLeftPulseLength;
    private int originalRightPulseLength;
    private int cycleLength;
    private int leftPulseLength;
    private int rightPulseLength;
    private AudioTrack track;
    private Object lock = new Object();

    public PwmMotorController(int sampleRate, int frequency) {
        this(sampleRate, frequency, 0, 0);
    }

    public PwmMotorController(int sampleRate, int frequency, double leftPulseMilliseconds, double rightPulseMilliseconds) {
        int bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        this.buffer = new short[bufferSize];
        this.sampleRate = sampleRate;
        this.originalCycleLength = sampleRate / frequency;
        this.originalLeftPulseLength = (int) (sampleRate * leftPulseMilliseconds / 1000);
        this.originalRightPulseLength = (int) (sampleRate * rightPulseMilliseconds / 1000);
        reset();
    }

    public void play() {
        if (track == null) {
            track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig, audioFormat, buffer.length, AudioTrack.MODE_STREAM);
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        // 画面を触っている間のみ音を鳴らす
                        short[] data = generatePwmData();
                        synchronized (lock) {
                            boolean released = track == null;
                            if (released) {
                                return;
                            }
                            if (track.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
                                track.write(data, 0, data.length);
                            }
                        }
                    }
                }
            }.start();
        }
        if (track.getPlayState() != AudioTrack.PLAYSTATE_PLAYING) {
            track.play();
        }
    }

    public void stop() {
        if (track != null) {
            track.stop();
        }
        reset();
    }

    public void release() {
        if (track != null) {
            synchronized (lock) {
                track.release();
                track = null;
            }
        }
        reset();
    }

    public short[] generatePwmData() {
        for (int i = 0; i < buffer.length; i++) {
            if ((i & 1) == 0) {
                buffer[i] = --leftPulseLength >= 0 ? Short.MAX_VALUE : Short.MIN_VALUE;
            } else {
                buffer[i] = --rightPulseLength >= 0 ? Short.MAX_VALUE : Short.MIN_VALUE;
                if (--cycleLength <= 0) {
                    reset();
                }
            }
        }
        return buffer;
    }

    public void reset() {
        leftPulseLength = originalLeftPulseLength;
        rightPulseLength = originalRightPulseLength;
        cycleLength = originalCycleLength;
    }

    public void setPulseMilliseconds(double leftPulseMilliseconds, double rightPulseMilliseconds) {
        originalLeftPulseLength = (int) (sampleRate * leftPulseMilliseconds / 1000);
        originalRightPulseLength = (int) (sampleRate * rightPulseMilliseconds / 1000);
        reset();
        Log.d("sound", "left: " + leftPulseMilliseconds + ", right: " + rightPulseMilliseconds);
    }

    public void setLeftPulseMilliseconds(double pulseMilliseconds) {
        originalLeftPulseLength = (int) (sampleRate * pulseMilliseconds / 1000);
        reset();
    }

    public void setRightPulseMilliseconds(double pulseMilliseconds) {
        originalRightPulseLength = (int) (sampleRate * pulseMilliseconds / 1000);
        reset();
    }

    public void setFrequency(int frequency) {
        originalCycleLength = sampleRate / frequency;
        reset();
    }
}

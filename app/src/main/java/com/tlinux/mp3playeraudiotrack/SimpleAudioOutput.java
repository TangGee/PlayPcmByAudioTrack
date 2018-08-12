package com.tlinux.mp3playeraudiotrack;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * Created by tlinux on 18-8-11.
 */

public class SimpleAudioOutput {

    private static final String TAG = "AudioOutputTrack";
    public static final int SAMPLES_PER_FRAME = 2;
    public static final int BYTES_PER_SAMPLE = 4;
    public static final int BYTES_PRE_FRAME = SAMPLES_PER_FRAME * BYTES_PER_SAMPLE;
    private AudioTrack mAudioTrack;
    private int mFrameRate;
    private int minBufferSize;

    public SimpleAudioOutput() {
        super();
    }

    public void start(int frameRate) {
        stop();
        mFrameRate = frameRate;
        mAudioTrack = createAudioTrack(frameRate);
        mAudioTrack.play();
    }

    public AudioTrack createAudioTrack(int frameRate) {
        int minBufferSizeBytes = AudioTrack.getMinBufferSize(frameRate,
                AudioFormat.CHANNEL_OUT_STEREO,AudioFormat.ENCODING_PCM_16BIT);
        Log.i(TAG, "AudioTrack.minBufferSize = " + minBufferSizeBytes
                                 + " bytes = " + (minBufferSizeBytes / BYTES_PRE_FRAME)
                              + " frames");
        int bufferSize = 8*minBufferSizeBytes/8;
        minBufferSize = bufferSize;
        int outputBufferSizeFrames = bufferSize/BYTES_PRE_FRAME;
        Log.i(TAG, "actual bufferSize = " + bufferSize + " bytes = " + outputBufferSizeFrames + " frames");

        AudioTrack player = new AudioTrack(AudioManager.STREAM_MUSIC,mFrameRate,
                AudioFormat.CHANNEL_OUT_STEREO,AudioFormat.ENCODING_PCM_16BIT,
                bufferSize,AudioTrack.MODE_STREAM);

        Log.i(TAG, "created AudioTrack");
        return player;
    }


    public int write(byte[] buffer, int offset, int length) {
        return mAudioTrack.write(buffer,offset,length);
    }

    public void stop() {
        if (mAudioTrack != null) {
            mAudioTrack.stop();
        }
    }

    public int getFrameRate() {
        return mFrameRate;
    }

    public AudioTrack getAudioTrack() {
        return mAudioTrack;
    }

    public int getMinBufferSize() {
        return minBufferSize;
    }

}

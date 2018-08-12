package com.tlinux.mp3playeraudiotrack;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private WorkHandler workHandler;
    private SimpleAudioOutput audioOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void play(View view) {
        if (audioOutput == null) {
            audioOutput = new SimpleAudioOutput();
        }
        if (workHandler == null) {
            HandlerThread handlerThread = new HandlerThread("player work");
            handlerThread.start();
            workHandler  = new WorkHandler(handlerThread.getLooper());
        }

        workHandler.sendEmptyMessage(WorkHandler.START);
    }

    private class WorkHandler extends Handler {

        public static final int START = 1;
        public WorkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START:
                    FileInputStream inputStream = null;
                    File file = new File("/sdcard/jiangzhende.pcm");
                    audioOutput.start(44100);
                    try {
                        inputStream = new FileInputStream(file);
                        byte buffer[] = new byte[audioOutput.getMinBufferSize()];
                        int len = 0;
                        while ((len = inputStream.read(buffer,0,buffer.length))>0) {
                            audioOutput.write(buffer,0,len);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (inputStream!=null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
            }
        }
    }


}

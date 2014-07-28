package com.robometrix;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder.AudioSource;
import android.util.Log;

/*
 * Thread to manage live recording/playback of voice input from the device's microphone.
 */
public class Audio extends Thread
{ 
    private boolean stopped = false;

    /**
     * Give the thread high priority so that it's not canceled unexpectedly, and start it
     */
    public Audio()
    { 
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
    }

    @Override
    public void run()
    {   	
        AudioRecord recorder = null;
        AudioTrack track = null;
        short[][]   buffers  = new short[256][160];
        int ix = 0;

        /*
         * Initialize buffer to hold continuously recorded audio data, start recording, and start
         * playback.
         */
        try
        {
        	final int HERTZ = 8000;
        	final int ENCODING = AudioFormat.ENCODING_PCM_16BIT;
            int N = AudioRecord.getMinBufferSize(HERTZ,AudioFormat.CHANNEL_IN_MONO,ENCODING);// *10
            recorder = new AudioRecord(AudioSource.MIC, HERTZ, AudioFormat.CHANNEL_IN_MONO, ENCODING, N);
            track = new AudioTrack(AudioManager.STREAM_MUSIC, HERTZ, 
                    AudioFormat.CHANNEL_OUT_MONO, ENCODING, N, AudioTrack.MODE_STREAM);
            recorder.startRecording();
            track.play();
            /*
             * Loops until something outside of this thread stops it.
             * Reads the data from the recorder and writes it to the audio track for playback.
             */

            while(!stopped)
            { 
                short[] buffer = buffers[ix++ % buffers.length];
                N = recorder.read(buffer,0,buffer.length);
                track.write(buffer, 0, buffer.length);
            }
        }
        catch(Throwable x)
        { 
            Log.w("Audio", "Error reading voice audio", x);
        }
        /*
         * Frees the thread's resources after the loop completes so that it can be run again
         */
        finally
        { 
            recorder.stop();
            recorder.release();
            track.stop();
            track.release();
        }
    }

    /**
     * Called from outside of the thread in order to stop the recording/playback loop
     */
    public void close()
    { 
         stopped = true;
    }

}
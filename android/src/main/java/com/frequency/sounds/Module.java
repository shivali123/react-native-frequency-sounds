package com.frequency.sounds;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;

import android.os.*;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

public class Module extends ReactContextBaseJavaModule {

  // private static final String DURATION_SHORT_KEY = "SHORT";
  // private static final String DURATION_LONG_KEY = "LONG";

  private int minTrackBufferSize;
  private static final int SAMPLING_RATE = 44100;
  private final short[] buffer = new short[minTrackBufferSize];

  public Module(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "FrequencySounds";
  }

  // @Override
  // public Map<String, Object> getConstants() {
  //   final Map<String, Object> constants = new HashMap<>();
  //   constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
  //   constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
  //   return constants;
  // }

  void playSound(){

    final AudioTrack  audioTrack = new AudioTrack(
         AudioManager.STREAM_MUSIC,
         SAMPLING_RATE,
         AudioFormat.CHANNEL_OUT_MONO,
         AudioFormat.ENCODING_PCM_16BIT,
         minTrackBufferSize,
         AudioTrack.MODE_STREAM);
  // audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
  //          sampleRate, AudioFormat.CHANNEL_OUT_MONO,
  //          AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
  //          AudioTrack.MODE_STATIC);
    audioTrack.write(buffer, 0, buffer.length);
    audioTrack.play();
   }

  void genTone(double frequency) {
    minTrackBufferSize = AudioTrack.getMinBufferSize(SAMPLING_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
    short[] buffer = new short[minTrackBufferSize];
    double f = frequency;
    double q = 0;
    double level = 16384;
    final double K = 2.0 * Math.PI / SAMPLING_RATE;

    for (int i = 0; i < minTrackBufferSize; i++) {
      f += (frequency - f) / 4096.0;
      q += (q < Math.PI) ? f * K : (f * K) - (2.0 * Math.PI);
      buffer[i] = (short) Math.round(Math.sin(q));
    }
  }

  @ReactMethod
  public void show(double frequency) {
    // Toast.makeText(getReactApplicationContext(), message, duration).show();
    genTone(frequency);
    playSound();

  }
}

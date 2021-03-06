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

  private static final String DURATION_SHORT_KEY = "SHORT";
  private static final String DURATION_LONG_KEY = "LONG";

  private final int duration = 100; // seconds
  private final int sampleRate = 8000;
  private final int numSamples = (duration * sampleRate)/1000;
  private final double sample[] = new double[numSamples];
  private final double freqOfTone = 220; // hz
  private final byte generatedSnd[] = new byte[2 * numSamples];

  public Module(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  public String getName() {
    return "FrequencySounds";
  }

  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
    constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
    return constants;
  }

  void playSound(){
    final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
           sampleRate, AudioFormat.CHANNEL_OUT_MONO,
           AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
           AudioTrack.MODE_STATIC);
    audioTrack.write(generatedSnd, 0, generatedSnd.length);
    audioTrack.play();
   }

  void genTone(double frequency){
    for (int i = 0; i < numSamples; ++i) {
        sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/frequency));
    }
    int idx = 0;
    for (final double dVal : sample) {
        // scale to maximum amplitude
        final short val = (short) ((dVal * 32767));
        // in 16 bit wav PCM, first byte is the low order byte
        generatedSnd[idx++] = (byte) (val & 0x00ff);
        generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

    }
  }

  @ReactMethod
  public void show(double frequency) {
    // Toast.makeText(getReactApplicationContext(), message, duration).show();
    genTone(frequency);
    playSound();

  }
}

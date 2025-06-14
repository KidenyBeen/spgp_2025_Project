package com.example.ajm1222.and.a2dg.framework.res;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.example.ajm1222.and.a2dg.framework.view.GameView;

import java.util.HashMap;

public class Sound {
    protected static MediaPlayer mediaPlayer;
    protected static SoundPool soundPool;

    private static float masterVolume = 1.0f;
    private static float bgmVolume = 1.0f;
    private static float effectVolume = 1.0f;


    private static final String PREFS_NAME = "sound_preferences";
    private static final String KEY_MASTER = "master_volume";
    private static final String KEY_BGM = "bgm_volume";
    private static final String KEY_SFX = "sfx_volume";


    private static float feffectVolume;

    public static void playMusic(int resId) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }


        mediaPlayer = MediaPlayer.create(GameView.view.getContext(), resId);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();


        SharedPreferences prefs = GameView.view.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Sound.setBgmVolume(prefs.getFloat(KEY_BGM, 1.0f));
        Sound.setMasterVolume(prefs.getFloat(KEY_MASTER, 1.0f));
        Sound.seteffectVolume(prefs.getFloat(KEY_SFX, 1.0f));
    }

    public static void setMasterVolume(float volume) {
        masterVolume = volume;
        applyVolume();

    }
    private static void applyVolume() {

        float fbgmVolume = bgmVolume * masterVolume;
        mediaPlayer.setVolume(fbgmVolume, fbgmVolume);
        feffectVolume = effectVolume * masterVolume;

    }
    public static void setBgmVolume(float volume)
    {
        bgmVolume = volume;
        mediaPlayer.setVolume(bgmVolume, bgmVolume);
    }

    public static void seteffectVolume(float volume)
    {
        effectVolume = volume;
    }
    public static void stopMusic() {
        if (mediaPlayer == null) return;
        mediaPlayer.stop();
        mediaPlayer = null;
    }
    public static void pauseMusic() {
        if (mediaPlayer == null) return;
        mediaPlayer.pause();
    }
    public static void resumeMusic() {
        if (mediaPlayer == null) return;
        mediaPlayer.start();
    }

    private static final HashMap<Integer, Integer> soundIdMap = new HashMap<>();
    public static void playEffect(int resId) {
        SoundPool pool = getSoundPool();
        int soundId;
        if (soundIdMap.containsKey(resId)) {
            soundId = soundIdMap.get(resId);
        } else {
            soundId = pool.load(GameView.view.getContext(), resId, 1);
            soundIdMap.put(resId, soundId);
        }

        // int streamId =

        SharedPreferences prefs = GameView.view.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        Sound.setMasterVolume(prefs.getFloat(KEY_MASTER, 1.0f));
        Sound.seteffectVolume(prefs.getFloat(KEY_SFX, 1.0f));

        pool.play(soundId, feffectVolume, feffectVolume, 1, 0, 1f);
    }

    private static SoundPool getSoundPool() {
        if (soundPool != null) return soundPool;

        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attrs)
                .setMaxStreams(3)
                .build();
        return soundPool;
    }
}

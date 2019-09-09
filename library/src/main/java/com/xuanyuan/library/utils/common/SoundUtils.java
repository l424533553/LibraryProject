package com.xuanyuan.library.utils.common;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;


import com.xuanyuan.library.R;

import static android.content.Context.AUDIO_SERVICE;


/**
 * 声音工具类 ，使用步骤
 * 1.initSoundPool()   初始化声音池
 * 2. play()     播放声音
 */
public class SoundUtils {

    private SoundPool sp;
    public Context context;
    private SparseIntArray soundArray;

    //初始化声音池
    public void initSoundPool(Context context) {
        this.context = context;
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        soundArray = new SparseIntArray();
        soundArray.put(1, sp.load(context, R.raw.msg, 1));

    }

    //播放声音池声音
    public void play(int sound, int number) {
        AudioManager am = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        //返回当前AlarmManager最大音量
        float audioMaxVolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        //返回当前AudioManager对象的音量值
        float audioCurrentVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumnRatio = audioCurrentVolume / audioMaxVolume;
        sp.play(
                soundArray.get(sound),//播放的音乐Id
                audioCurrentVolume, //左声道音量
                volumnRatio, //右声道音量
                1, //优先级，0为最低
                number, //循环次数，0无不循环，-1无永远循环
                1);//回放速度，值在0.5-2.0之间，1为正常速度
    }
}


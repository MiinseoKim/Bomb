package org.androidtown.myapplication123;

/**
 * Created by choiyeonjoo on 16. 9. 20..
 */
import android.content.Context;
import android.media.MediaPlayer;

public class MySoundPlay {
    MediaPlayer mp = null;
    public MySoundPlay ( Context context, int id ) {
        mp = MediaPlayer.create(context, id);
    }
    public void play() {
        mp.seekTo(0);
        mp.setLooping(false);
        mp.start();

    }
}

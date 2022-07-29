package com.anns.appnghenhacso.API;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.anns.appnghenhacso.MainActivity;
import com.anns.appnghenhacso.Model.Item;
import com.anns.appnghenhacso.R;
import com.anns.appnghenhacso.Screen.NotificationReceiver;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.anns.appnghenhacso.Fragment.FragmentBottomMusic.*;
import static com.anns.appnghenhacso.Screen.Detail_Music.*;

public class MusicService extends Service {
    IBinder iBinder = new MyBinder();
    public MediaPlayer mediaPlayer;
    ArrayList<Item> musicFiles = new ArrayList<Item>();
    Uri uri;
    SharedPreferences pref;
    String value;
    int position = 0;
    ArrayList<Item> item_mini;
    MediaSessionCompat mediaSession;
    ActionPlaying actionPlaying;

    @Override
    public void onCreate() {
        super.onCreate();
        musicFiles = mangbaihat;
        mediaSession = new MediaSessionCompat(this, "PlayerAudio");
        pref = getApplicationContext().getSharedPreferences("PREF", MODE_PRIVATE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class MyBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String actionName = intent.getStringExtra("myActionName");
        if (actionName != null) {
            switch (actionName) {
                case "ActionPrev":
                    actionPlaying.prevClick();
                    break;
                case "ActionPlay":
                    actionPlaying.playClick();
                    break;
                case "ActionPause":
                    System.out.println("Pause pause");
                    actionPlaying.playClick();
                    break;
                case "ActionNext":
                    actionPlaying.nextClick();
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public void start() {
        mediaPlayer.start();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void setDataSource(String uri) {
        try {
            mediaPlayer.setDataSource(this, Uri.parse(uri));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void stop() {
        mediaPlayer.stop();
    }

    public void release() {
        mediaPlayer.release();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public void seekTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public void createMediaPlayer(String url) {
        uri = Uri.parse(url);
        mediaPlayer = MediaPlayer.create(getBaseContext(), uri);
    }

    public void setOnCompletionListener() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                next = true;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setCallBack(ActionPlaying actionPlaying) {
        this.actionPlaying = actionPlaying;
        System.out.println("Đã set");
    }

    private Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}

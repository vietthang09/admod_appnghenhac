package com.anns.appnghenhacso.Screen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.anns.appnghenhacso.API.ActionPlaying;
import com.anns.appnghenhacso.API.MusicService;
import com.anns.appnghenhacso.APIYouTube.APIServiceSearchYT;
import com.anns.appnghenhacso.APIYouTube.DataserviceYT;
import com.anns.appnghenhacso.MainActivity;
import com.anns.appnghenhacso.Model.Item;
import com.anns.appnghenhacso.ModelYT.ItemDetailVideoYT;
import com.anns.appnghenhacso.ModelYT.ItemYT;
import com.anns.appnghenhacso.ModelYT.RecordDetailVideoYT;
import com.anns.appnghenhacso.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoViewYT extends AppCompatActivity implements ActionPlaying{
    VideoView videoview;
    String idVideo;
    String url;
    public static MusicService musicService;
    ArrayList<ItemDetailVideoYT> item;
    MediaSessionCompat mediaSession;
    ArrayList<ItemYT> itemYTS;
    public static int position = 0;
    Bitmap picture;
    PendingIntent pendingIntent,pausependingIntent,nextpendingIntent,prevpendingIntent;
    LinearLayout loading_new;
    Button button,btn144dp,btn360dp,btn720dp;
    LinearLayout ln_grp_dp;
    private  int duration = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view_yt);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        videoview = (VideoView) findViewById(R.id.videoView);
        mediaSession = new MediaSessionCompat(this,"PlayerAudio");
        button = findViewById(R.id.button);
        btn144dp = findViewById(R.id.btn144dp);
        btn360dp = findViewById(R.id.btn360dp);
        btn720dp = findViewById(R.id.btn720dp);
        ln_grp_dp = findViewById(R.id.ln_grp_dp);

        loading_new = findViewById(R.id.loading_new);
        loading_new.setVisibility(View.VISIBLE);
        videoview.setVisibility(View.INVISIBLE);
        button.setVisibility(View.INVISIBLE);
        ln_grp_dp.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        if(intent.hasExtra("idVideo")){
            idVideo = intent.getStringExtra("idVideo");
            System.out.println("Đây là idVideo" + idVideo);
        }
        if (intent.hasExtra("cacbaihat")){
            ArrayList<ItemYT> items = (ArrayList<ItemYT>) intent.getSerializableExtra("cacbaihat");
            itemYTS = items;
        }
        if(intent.hasExtra("index")){
            position = intent.getIntExtra("index",0);
            System.out.println("Đã set lại pos "+position);
        }
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
                musicService = myBinder.getService();
                musicService.setCallBack(VideoViewYT.this);
                if(musicService.mediaPlayer!=null){
                    Intent intent = getIntent();
                    musicService.stop();
                    if(intent.hasExtra("CONTINUE")){

                    }else{
                        musicService.stop();
                    }
                }
                getDataDetail();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                musicService = null;
            }
        };
        Intent serviceStartIntent = new Intent(this,MusicService.class);
        bindService(serviceStartIntent, serviceConnection,BIND_AUTO_CREATE);

    }

    private void eventClick(){
        btn144dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.size()!=0){
                    duration = videoview.getCurrentPosition();
                    System.out.println(duration+ "duration hiện tại");
                    url = item.get(0).getUrl();
                    videoview.pause();
                    videoview.setVideoURI(Uri.parse(url));
//                    musicService.setDataSource(url);
                    videoview.requestFocus();
                    videoview.seekTo(duration);
                    videoview.start();
                    Toast.makeText(VideoViewYT.this,"Đang chuyển đổi chất lượng 144dp",Toast.LENGTH_LONG).show();
                }

            }
        });

        btn360dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                duration = videoview.getCurrentPosition();
                videoview.pause();
                url = item.get(1).getUrl();
                videoview.setVideoURI(Uri.parse(url));
//                musicService.setDataSource(url);
                videoview.requestFocus();
                videoview.seekTo(duration);
                videoview.start();
                Toast.makeText(VideoViewYT.this,"Đang chuyển đổi chất lượng 360dp",Toast.LENGTH_LONG).show();
            }
        });

        btn720dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                duration = videoview.getCurrentPosition();
                videoview.pause();
                url = item.get(2).getUrl();
                videoview.setVideoURI(Uri.parse(url));
//                musicService.setDataSource(url);
                videoview.requestFocus();
                videoview.seekTo(duration);
                videoview.start();
                Toast.makeText(VideoViewYT.this,"Đang chuyển đổi chất lượng 720dp",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getDataDetail(){
        DataserviceYT dataserviceYT = APIServiceSearchYT.getService();
        Call<RecordDetailVideoYT> callback = dataserviceYT.GetDetailVideo(idVideo);
        callback.enqueue(new Callback<RecordDetailVideoYT>() {
            @Override
            public void onResponse(Call<RecordDetailVideoYT> call, Response<RecordDetailVideoYT> response) {
                RecordDetailVideoYT recordDetailVideoYT = response.body();
                item = recordDetailVideoYT.getItemDetailVideoYTS();

                url = item.get(1).getUrl();
                musicService.createMediaPlayer(url);
                Uri uri = Uri.parse(url);
                videoview.setVideoURI(uri);
                videoview.start();
                TestNotify();
                loading_new.setVisibility(View.INVISIBLE);
                videoview.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                ln_grp_dp.setVisibility(View.VISIBLE);
                eventClick();
//                UpdateTime();
            }

            @Override
            public void onFailure(Call<RecordDetailVideoYT> call, Throwable t) {

            }
        });
    }

    public void onButtonClick(View v) {
        if(videoview.isPlaying()){
            videoview.pause();
        }else{
            videoview.start();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
            videoview.pause();
            musicService.seekTo(videoview.getCurrentPosition());
            musicService.start();
            setArrMusicMini();
            return false; //I have tried here true also
        }
        return super.onKeyDown(keyCode, event);
    }
    private void UpdateTime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(videoview != null){
                    duration = videoview.getCurrentPosition();
                }
            }
        },300);
    }
    private void setArrMusicMini(){
        SharedPreferences pref = getSharedPreferences("PREF", MODE_PRIVATE);
        Gson gson = new Gson();
        pref.edit().putString("@Arr_Mini", null).commit();
        pref.edit().putString("@Position_Music_Bottom", null).commit();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Logic to turn on the screen

//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

//            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
//
//            if (!powerManager.isInteractive()){ // if screen is not already on, turn it on (get wake_lock for 10 seconds)
//                PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MH24_SCREENLOCK");
//                wl.acquire(10000);
//                PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MH24_SCREENLOCK");
//                wl_cpu.acquire(10000);
//            }
            CharSequence name = "Test";
            String description = "Đây là bản test";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setShowBadge(true);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void TestNotify(){

        createNotificationChannel();
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, Search_Result.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction("ActionPrev");
        Intent pauseIntent = new Intent(this, NotificationReceiver.class).setAction("ActionPause");
        Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction("ActionNext");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
            pausependingIntent = PendingIntent.getBroadcast(this,0,pauseIntent,PendingIntent.FLAG_MUTABLE);
            nextpendingIntent = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_MUTABLE);
            prevpendingIntent = PendingIntent.getBroadcast(this,0,prevIntent,PendingIntent.FLAG_MUTABLE);
        }
        else{

            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            nextpendingIntent = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            pausependingIntent = PendingIntent.getBroadcast(this,0,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            prevpendingIntent = PendingIntent.getBroadcast(this,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            System.out.println("zô nào");
        }

        picture = getBitmapFromURL(itemYTS.get(position).getThumbnail());


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(picture)
                .setContentTitle(itemYTS.get(position).getTitle())
                .setContentText(itemYTS.get(position).getOwnerChannelText())
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .addAction(R.drawable.iconpreview_mini,"Previous",prevpendingIntent)
                .addAction(R.drawable.iconpause_mini,"Pause",pausependingIntent)
                .addAction(R.drawable.iconnext_mini,"Next",nextpendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().
                        setShowActionsInCompactView(1 /* #1: pause button */).
                        setMediaSession(mediaSession.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true);
        builder.setOngoing(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
    }

    private void NotifyPause(){

        createNotificationChannel();
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, Search_Result.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction("ActionPrev");
        Intent pauseIntent = new Intent(this, NotificationReceiver.class).setAction("ActionPause");
        Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction("ActionNext");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE);
            pausependingIntent = PendingIntent.getBroadcast(this,0,pauseIntent,PendingIntent.FLAG_MUTABLE);
            nextpendingIntent = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_MUTABLE);
            prevpendingIntent = PendingIntent.getBroadcast(this,0,prevIntent,PendingIntent.FLAG_MUTABLE);
        }
        else{

            pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            nextpendingIntent = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            pausependingIntent = PendingIntent.getBroadcast(this,0,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            prevpendingIntent = PendingIntent.getBroadcast(this,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            System.out.println("zô nào");
        }

        picture = getBitmapFromURL(itemYTS.get(position).getThumbnail());


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(picture)
                .setContentTitle(itemYTS.get(position).getTitle())
                .setContentText(itemYTS.get(position).getOwnerChannelText())
                .setDefaults(NotificationCompat.DEFAULT_LIGHTS)
                .addAction(R.drawable.iconpreview_mini,"Previous",prevpendingIntent)
                .addAction(R.drawable.iconplay_mini,"Pause",pausependingIntent)
                .addAction(R.drawable.iconnext_mini,"Next",nextpendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().
                        setShowActionsInCompactView(1 /* #1: pause button */).
                        setMediaSession(mediaSession.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
                .setAutoCancel(true);
        builder.setOngoing(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, builder.build());
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

    @Override
    public void nextClick() {
        Toast.makeText(this,"Bạn đang nghe nhạc youtube, không thể chuyển bài",Toast.LENGTH_LONG).show();
    }

    @Override
    public void prevClick() {
        Toast.makeText(this,"Bạn đang nghe nhạc youtube, không thể chuyển bài",Toast.LENGTH_LONG).show();
    }

    @Override
    public void playClick() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        System.out.println(cn.getClassName() +"hey hey");
        if(cn.getClassName().equals("com.anns.appnghenhacso.Screen.VideoViewYT")){
            if(videoview.isPlaying()){
                videoview.pause();
                NotifyPause();
            }else{
                videoview.start();
                TestNotify();
            }
        }else{
            if (musicService.isPlaying()){
                musicService.pause();
                NotifyPause();
            }else {
                musicService.start();
                TestNotify();
            }
        }

    }
}
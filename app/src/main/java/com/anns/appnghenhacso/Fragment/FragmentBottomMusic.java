package com.anns.appnghenhacso.Fragment;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.*;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import com.anns.appnghenhacso.API.ActionPlaying;
import com.anns.appnghenhacso.API.MusicService;
import com.anns.appnghenhacso.MainActivity;
import com.anns.appnghenhacso.Model.Item;
import com.anns.appnghenhacso.R;
import com.anns.appnghenhacso.Screen.Detail_Music;
import com.anns.appnghenhacso.Screen.NotificationReceiver;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class FragmentBottomMusic extends Fragment implements ActionPlaying {
    public static ImageView img_bottom_music,prev_bottom_music,play_bottom_music,next_bottom_music;
    private TextView song_name_bottom_music,compose_bottom_music,txtTimesong,txtTotaltimesong;
    SeekBar sktime;
    MediaPlayer mediaPlayer;
    ArrayList<Item> item_mini;
    public static MusicService musicService;
    String value;
    SharedPreferences pref;
    MediaSessionCompat mediaSession;
    int position = 0;
    public static boolean next1 = false;
    Bitmap picture;
    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_music, container, false);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        img_bottom_music = (ImageView) view.findViewById(R.id.img_bottom_music);
        prev_bottom_music = (ImageView) view.findViewById(R.id.prev_bottom_music);
        next_bottom_music = (ImageView) view.findViewById(R.id.next_bottom_music);
        play_bottom_music = (ImageView) view.findViewById(R.id.play_bottom_music);

        song_name_bottom_music = (TextView) view.findViewById(R.id.song_name_bottom_music);
        compose_bottom_music = (TextView) view.findViewById(R.id.compose_bottom_music);
        txtTimesong = (TextView) view.findViewById(R.id.textviewtimesong1);
        txtTotaltimesong = (TextView) view.findViewById(R.id.textviewtotaltimesong1);
        sktime = (SeekBar) view.findViewById(R.id.seekbarsong1);

        mediaSession = new MediaSessionCompat(getActivity(),"PlayerAudio");
        pref = this.getActivity().getSharedPreferences("PREF", MODE_PRIVATE);
        value = pref.getString("@Arr_Mini", null);
        if(value==null){
            view.setVisibility(View.GONE);
        }else{
            ServiceConnection serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
                    musicService = myBinder.getService();
                    musicService.setCallBack(FragmentBottomMusic.this);
                    String stringpos =  pref.getString("@Position_Music_Bottom", null);
                    if(stringpos!=null){
                        position = Integer.parseInt(stringpos);
                    }
                    init();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    musicService = null;
                }
            };
            Intent serviceStartIntent = new Intent(getActivity(),MusicService.class);
            getActivity().bindService(serviceStartIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }


//        view.setVisibility(View.GONE);
        return view;
    }


    private void init(){
        if(musicService.mediaPlayer!=null){
            TimeSong();
            UpdateTime();
            if(musicService.isPlaying()){
                play_bottom_music.setImageResource(R.drawable.iconpause);
            }else{
                play_bottom_music.setImageResource(R.drawable.iconplay);
            }
        }
        JsonArray jArray = new JsonParser().parse(value).getAsJsonArray();
        Type type = new TypeToken<ArrayList<Item>>() {}.getType();
        Gson gson = new Gson();
        item_mini = gson.fromJson(jArray,type);
        String hinhanh = item_mini.get(position).getFields().getImage_Url();
        song_name_bottom_music.setText(truncate(item_mini.get(position).getFields().getMusic_Title()));
        compose_bottom_music.setText(item_mini.get(position).getFields().getComposed());
        if(hinhanh.contains("/storage/emulated/0/Download")){
            final BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap scaledBitmap = BitmapFactory.decodeFile(hinhanh, options);
            img_bottom_music.setImageBitmap(scaledBitmap);
        }else{
            Picasso.get().load(hinhanh).into(img_bottom_music);
        }
        eventClick();

    }
    private String truncate (String text){
        if(text.length()>20){
            return text.substring(0, 17) + "...";
        }else{
            return text;
        }
    }
    public void eventClick(){
        play_bottom_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicService.mediaPlayer==null){
                    play_bottom_music.setImageResource(R.drawable.iconpause);
                    musicService.createMediaPlayer(item_mini.get(position).getFields().getMusic_Url());
                    musicService.start();
                    TimeSong();
                    UpdateTime();
                    TestNotify();
                }
                else{
                    if(musicService.isPlaying()){
                        play_bottom_music.setImageResource(R.drawable.iconplay);
                        musicService.pause();
                        NotifyPause();
                    }else{
                        musicService.start();
                        play_bottom_music.setImageResource(R.drawable.iconpause);
                        TestNotify();
                    }
                }
            }
        });
        prev_bottom_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item_mini.size() > 0){
                    if(musicService.mediaPlayer != null) {
                        if (musicService.isPlaying()) {
                            musicService.stop();
                            musicService.release();
//                        mediaPlayer = null;
                        }
                    }
                    if (position < (item_mini.size())){
                        play_bottom_music.setImageResource(R.drawable.iconpause);
                        position--;
                        pref.edit().putString("@Position_Music_Bottom", String.valueOf(position)).commit();
                        if (position < 0 ){
                            position = item_mini.size() - 1;
                            pref.edit().putString("@Position_Music_Bottom", String.valueOf(0)).commit();
                        }

                        musicService.createMediaPlayer(item_mini.get(position).getFields().getMusic_Url());
                        musicService.start();
                        song_name_bottom_music.setText(truncate(item_mini.get(position).getFields().getMusic_Title()));
                        compose_bottom_music.setText(item_mini.get(position).getFields().getComposed());
                        String hinhanh = item_mini.get(position).getFields().getImage_Url();
                        if(hinhanh.contains("/storage/emulated/0/Download")){
                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            Bitmap scaledBitmap = BitmapFactory.decodeFile(hinhanh, options);
                            img_bottom_music.setImageBitmap(scaledBitmap);
                        }else{
                            Picasso.get().load(hinhanh).into(img_bottom_music);
                        }
                        TimeSong();
                        UpdateTime();
                        TestNotify();
                    }
                }
                prev_bottom_music.setClickable(false);
                next_bottom_music.setClickable(false);
                TestNotify();
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prev_bottom_music.setClickable(true);
                        next_bottom_music.setClickable(true);
                    }
                },5000);
            }
        });
        next_bottom_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item_mini.size() > 0){
                    if(musicService.mediaPlayer != null) {
                        if (musicService.isPlaying()) {
                            musicService.stop();
                            musicService.release();
//                        mediaPlayer = null;
                        }
                    }
                    if (position < (item_mini.size())){
                        play_bottom_music.setImageResource(R.drawable.iconpause);
                        position++;
                        pref.edit().putString("@Position_Music_Bottom", String.valueOf(position)).commit();
                        if (position > (item_mini.size() - 1)){
                            position = 0;
                            pref.edit().putString("@Position_Music_Bottom", String.valueOf(0)).commit();
                        }
                        musicService.createMediaPlayer(item_mini.get(position).getFields().getMusic_Url());
                        musicService.start();
                        song_name_bottom_music.setText(truncate(item_mini.get(position).getFields().getMusic_Title()));
                        compose_bottom_music.setText(item_mini.get(position).getFields().getComposed());
                        String hinhanh = item_mini.get(position).getFields().getImage_Url();
                        if(hinhanh.contains("/storage/emulated/0/Download")){
                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            Bitmap scaledBitmap = BitmapFactory.decodeFile(hinhanh, options);
                            img_bottom_music.setImageBitmap(scaledBitmap);
                        }else{
                            Picasso.get().load(hinhanh).into(img_bottom_music);
                        }
                        TimeSong();
                        UpdateTime();
                        TestNotify();
//                        new PlayMp3().execute(mangbaihat.get(position).getFields().getMusic_Url());
                    }
                }
                prev_bottom_music.setClickable(false);
                next_bottom_music.setClickable(false);
                TestNotify();
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        prev_bottom_music.setClickable(true);
                        next_bottom_music.setClickable(true);
                    }
                },5000);
            }
        });
        sktime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println("Đây là "+seekBar.getProgress());
                if(musicService.mediaPlayer!=null){
                    musicService.seekTo(seekBar.getProgress());
                }
            }
        });
        img_bottom_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),Detail_Music.class);
                intent.putExtra("CONTINUE","");
                intent.putExtra("cacbaihat",item_mini);
                intent.putExtra("index",position);
                startActivity(intent);
            }
        });
    }

    private void TimeSong() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txtTotaltimesong.setText(simpleDateFormat.format(musicService.getDuration()));
        sktime.setMax(musicService.getDuration());
    }
    private void UpdateTime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (musicService != null){
                    sktime.setProgress(musicService.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    txtTimesong.setText(simpleDateFormat.format(musicService.getCurrentPosition()));
                    handler.postDelayed(this,300);
                    musicService.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            next1 = true;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
//                    musicService.setOnCompletionListener();

                }
            }
        },300);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (next1 == true){
                    if (position < (item_mini.size())){
                        play_bottom_music.setImageResource(R.drawable.iconpause);
                        position++;
                        pref.edit().putString("@Position_Music_Bottom", String.valueOf(position)).commit();

                        if (position > (item_mini.size() - 1)){
                            position = 0;
                        }
                        musicService.createMediaPlayer(item_mini.get(position).getFields().getMusic_Url());
                        musicService.start();
                        TimeSong();
                        UpdateTime();
//                        new PlayMp3().execute(mangbaihat.get(position).getFields().getMusic_Url());
                        song_name_bottom_music.setText(truncate(item_mini.get(position).getFields().getMusic_Title()));
                        compose_bottom_music.setText(item_mini.get(position).getFields().getComposed());
                        String hinhanh = item_mini.get(position).getFields().getImage_Url();
                        if(hinhanh.contains("/storage/emulated/0/Download")){
                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            Bitmap scaledBitmap = BitmapFactory.decodeFile(hinhanh, options);
                            img_bottom_music.setImageBitmap(scaledBitmap);
                        }else{
                            Picasso.get().load(hinhanh).into(img_bottom_music);
                        }
                    }
                    prev_bottom_music.setClickable(false);
                    next_bottom_music.setClickable(false);
                    TestNotify();
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            prev_bottom_music.setClickable(true);
                            next_bottom_music.setClickable(true);
                        }
                    },5000);
                    next1 = false;
                    handler1.removeCallbacks(this,1000);
                }else {
                    handler1.postDelayed(this,1000);
                }
            }
        },1000);
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Test";
            String description = "Đây là bản test";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =  getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void TestNotify(){
        if(getContext()!=null) {
            createNotificationChannel();
            // Create an explicit intent for an Activity in your app
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Intent prevIntent = new Intent(getContext(), NotificationReceiver.class).setAction("ActionPrev");
            Intent pauseIntent = new Intent(getContext(), NotificationReceiver.class).setAction("ActionPause");
            Intent nextIntent = new Intent(getContext(), NotificationReceiver.class).setAction("ActionNext");

            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
            PendingIntent prevpendingIntent = PendingIntent.getBroadcast(getContext(), 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent pausependingIntent = PendingIntent.getBroadcast(getContext(), 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent nextpendingIntent = PendingIntent.getBroadcast(getContext(), 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if(item_mini.get(position).getFields().getImage_Url().contains("/storage/emulated/0/Download")){
                final BitmapFactory.Options options = new BitmapFactory.Options();
                picture = BitmapFactory.decodeFile(item_mini.get(position).getFields().getImage_Url(), options);
            }else{
                picture = getBitmapFromURL(item_mini.get(position).getFields().getImage_Url());
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "CHANNEL_ID")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(picture)
                    .setContentTitle(item_mini.get(position).getFields().getMusic_Title())
                    .setContentText(item_mini.get(position).getFields().getComposed())
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .addAction(R.drawable.iconpreview_mini, "Previous", prevpendingIntent)
                    .addAction(R.drawable.iconpause_mini, "Pause", pausependingIntent)
                    .addAction(R.drawable.iconnext_mini, "Next", nextpendingIntent)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().
                            setShowActionsInCompactView(1 /* #1: pause button */).
                            setMediaSession(mediaSession.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setOnlyAlertOnce(true)
                    .setAutoCancel(true);
            builder.setOngoing(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, builder.build());
        }

    }

    private void NotifyPause(){
        if(getContext()!=null) {
            createNotificationChannel();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            Intent prevIntent = new Intent(getActivity(), NotificationReceiver.class).setAction("ActionPrev");
            Intent playIntent = new Intent(getActivity(), NotificationReceiver.class).setAction("ActionPlay");
            Intent nextIntent = new Intent(getActivity(), NotificationReceiver.class).setAction("ActionNext");

            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
            PendingIntent prevpendingIntent = PendingIntent.getBroadcast(getActivity(), 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent nextpendingIntent = PendingIntent.getBroadcast(getActivity(), 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            PendingIntent playpendingIntent = PendingIntent.getBroadcast(getActivity(), 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if(item_mini.get(position).getFields().getImage_Url().contains("/storage/emulated/0/Download")){
                final BitmapFactory.Options options = new BitmapFactory.Options();
                picture = BitmapFactory.decodeFile(item_mini.get(position).getFields().getImage_Url(), options);
            }else{
                picture = getBitmapFromURL(item_mini.get(position).getFields().getImage_Url());
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "CHANNEL_ID")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(picture)
                    .setContentTitle(item_mini.get(position).getFields().getMusic_Title())
                    .setContentText(item_mini.get(position).getFields().getComposed())
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .addAction(R.drawable.iconpreview_mini, "Previous", prevpendingIntent)
                    .addAction(R.drawable.iconplay_mini, "Play", playpendingIntent)
                    .addAction(R.drawable.iconnext_mini, "Next", nextpendingIntent)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().
                            setShowActionsInCompactView(1 /* #1: pause button */).
                            setMediaSession(mediaSession.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    // Set the intent that will fire when the user taps the notification
                    .setContentIntent(pendingIntent)
                    .setOnlyAlertOnce(true)
                    .setAutoCancel(true);
            builder.setOngoing(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());

            notificationManager.notify(0, builder.build());
        }
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
        if (item_mini.size() > 0){
            if(musicService.mediaPlayer != null) {
                if (musicService.isPlaying()) {
                    musicService.stop();
                    musicService.release();
//                        mediaPlayer = null;
                }
            }
            if (position < (item_mini.size())){
                play_bottom_music.setImageResource(R.drawable.iconpause);
                position++;
                pref.edit().putString("@Position_Music_Bottom", String.valueOf(position)).commit();
                if (position > (item_mini.size() - 1)){
                    position = 0;
                    pref.edit().putString("@Position_Music_Bottom", String.valueOf(0)).commit();
                }
                musicService.createMediaPlayer(item_mini.get(position).getFields().getMusic_Url());
                musicService.start();
                song_name_bottom_music.setText(truncate(item_mini.get(position).getFields().getMusic_Title()));
                compose_bottom_music.setText(item_mini.get(position).getFields().getComposed());
                String hinhanh = item_mini.get(position).getFields().getImage_Url();
                if(hinhanh.contains("/storage/emulated/0/Download")){
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap scaledBitmap = BitmapFactory.decodeFile(hinhanh, options);
                    img_bottom_music.setImageBitmap(scaledBitmap);
                }else{
                    Picasso.get().load(hinhanh).into(img_bottom_music);
                }
                TimeSong();
                UpdateTime();
                TestNotify();
//                        new PlayMp3().execute(mangbaihat.get(position).getFields().getMusic_Url());
            }
        }
        prev_bottom_music.setClickable(false);
        next_bottom_music.setClickable(false);
        TestNotify();
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                prev_bottom_music.setClickable(true);
                next_bottom_music.setClickable(true);
            }
        },5000);
    }

    @Override
    public void prevClick() {
        if (item_mini.size() > 0){
            if(musicService.mediaPlayer != null) {
                if (musicService.isPlaying()) {
                    musicService.stop();
                    musicService.release();
//                        mediaPlayer = null;
                }
            }
            if (position < (item_mini.size())){
                play_bottom_music.setImageResource(R.drawable.iconpause);
                position--;
                pref.edit().putString("@Position_Music_Bottom", String.valueOf(position)).commit();
                if (position < 0 ){
                    position = item_mini.size() - 1;
                    pref.edit().putString("@Position_Music_Bottom", String.valueOf(0)).commit();
                }

                musicService.createMediaPlayer(item_mini.get(position).getFields().getMusic_Url());
                musicService.start();
                song_name_bottom_music.setText(truncate(item_mini.get(position).getFields().getMusic_Title()));
                compose_bottom_music.setText(item_mini.get(position).getFields().getComposed());
                String hinhanh = item_mini.get(position).getFields().getImage_Url();
                if(hinhanh.contains("/storage/emulated/0/Download")){
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap scaledBitmap = BitmapFactory.decodeFile(hinhanh, options);
                    img_bottom_music.setImageBitmap(scaledBitmap);
                }else{
                    Picasso.get().load(hinhanh).into(img_bottom_music);
                }
                TimeSong();
                UpdateTime();
                TestNotify();
            }
        }
        prev_bottom_music.setClickable(false);
        next_bottom_music.setClickable(false);
        TestNotify();
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                prev_bottom_music.setClickable(true);
                next_bottom_music.setClickable(true);
            }
        },5000);
    }

    @Override
    public void playClick() {
        if(musicService.mediaPlayer==null){
            play_bottom_music.setImageResource(R.drawable.iconpause);
            musicService.createMediaPlayer(item_mini.get(position).getFields().getMusic_Url());
            musicService.start();
            TimeSong();
            UpdateTime();
            TestNotify();
        }
        else{
            if(musicService.isPlaying()){
                play_bottom_music.setImageResource(R.drawable.iconplay);
                musicService.pause();
                NotifyPause();
            }else{
                musicService.start();
                play_bottom_music.setImageResource(R.drawable.iconpause);
                TestNotify();
            }
        }
    }
}

package com.anns.appnghenhacso.presentation.Screen;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.*;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;
import com.anns.appnghenhacso.data.remote.API.ActionPlaying;
import com.anns.appnghenhacso.data.remote.API.MusicService;
import com.anns.appnghenhacso.presentation.Adapter.ViewPagerPlayListNhac;
import com.anns.appnghenhacso.presentation.Fragment.FragmentDowload;
import com.anns.appnghenhacso.presentation.Fragment.FragmentFavorite;
import com.anns.appnghenhacso.presentation.Fragment.FragmentNew;
import com.anns.appnghenhacso.presentation.Fragment.Fragment_Dia_Nhac;
import com.anns.appnghenhacso.presentation.Fragment.Fragment_Play_danh_sach_cac_bai_hat;
import com.anns.appnghenhacso.MainActivity;
import com.anns.appnghenhacso.domain.model.Model.Item;
import com.anns.appnghenhacso.R;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class Detail_Music extends AppCompatActivity implements ActionPlaying {
    Toolbar toolbarplaynhac;
    TextView txtTimesong,txtTotaltimesong;
    SeekBar sktime;
    public  static ImageButton imgplay,imgrepeat,imgpreview,imgnext,imgrandom,btn_favorite,btn_download;
//    MediaPlayer mediaPlayer;
    public static Fragment_Dia_Nhac fragment_dia_nhac;
    Fragment_Play_danh_sach_cac_bai_hat fragment_play_danh_sach_cac_bai_hat;
    public static ViewPagerPlayListNhac adapternhac;
    ViewPager viewPagerplaynhac;
    private AdView mAdView;
    private RewardedAd rewardedAd;
    public static int position = 0;
    boolean repeat = false;
    boolean checkrandom = false;
    public static boolean next = false;
    boolean status_favorite = false;
    boolean status_download = false;
    public  static ArrayList<Item> mangbaihat = new ArrayList<>();
    private static final int PERMISSION_STORAGE_CODE = 1000;
    public static MusicService musicService;
    MediaSessionCompat mediaSession;

    private InterstitialAd mInterstitialAd;
    Bitmap picture;
    private RewardedAd mRewardedAd ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__music);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btn_favorite = (ImageButton) findViewById(R.id.btn_favorite);
        btn_download = (ImageButton) findViewById(R.id.btn_download);
        mediaSession = new MediaSessionCompat(this,"PlayerAudio");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adsdetailView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        if(intent.hasExtra("index")){
            position = intent.getIntExtra("index",0);
            System.out.println("Đã set lại pos "+position);
        }
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
                musicService = myBinder.getService();
                musicService.setCallBack(Detail_Music.this);
                if(musicService.mediaPlayer!=null){
                    Intent intent = getIntent();
                    if(intent.hasExtra("CONTINUE")){

                    }else{
                        musicService.stop();
                    }
                }
                GetDataFromIntent();
                init();
                eventClick();
                checkFavorite();
                checkDowload();
                downLoad();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                musicService = null;
            }
        };
        Intent serviceStartIntent = new Intent(this,MusicService.class);
        bindService(serviceStartIntent, serviceConnection,BIND_AUTO_CREATE);

    }

    private void downLoad() {
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status_download){
                    Toast.makeText(getApplicationContext(),"Nhạc đã tải, vui lòng kiểm tra lại...",Toast.LENGTH_SHORT).show();
                }else{
                    try{
                        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
                            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                            requestPermissions(permissions,PERMISSION_STORAGE_CODE);
                        }else{
                            startDowload();
                        }
                    }catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
        });

    }
    private void startDowload(){
        String path_mp3 = startDowloadMp3();
        String path_img = startDowloadImage();

        ArrayList<Item> item_dowload = new ArrayList<>();
        item_dowload.add(mangbaihat.get(position));
        item_dowload.get(0).getFields().setMusic_Url(path_mp3);
        item_dowload.get(0).getFields().setImage_Url(path_img);

        SharedPreferences pref = getSharedPreferences("PREF", MODE_PRIVATE);
        //Get data string
        String value = pref.getString("@MusicDownload", null);
        if(value==null){
            Gson gson = new Gson();
            String json = gson.toJson(item_dowload);
            pref.edit().putString("@MusicDownload", json).commit();
        }else{
            JsonArray jArray = new JsonParser().parse(value).getAsJsonArray();
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<Item> arr_to_commit = gson.fromJson(jArray,type);
            arr_to_commit.add(item_dowload.get(0));
            String json = gson.toJson(arr_to_commit);
            pref.edit().putString("@MusicDownload", json).commit();
        }
        status_download = true;
        btn_download.setImageResource(R.drawable.ic_cloud_done_white);
        Toast.makeText(this,"Tải nhạc thành công ...",Toast.LENGTH_SHORT).show();
        FragmentDowload f = (FragmentDowload) FragmentDowload.myFragment;
        f.getFragmentManager().findFragmentByTag("4");
        if (f!= null) {
            f.refreshFragment();
        }
    }
    private String startDowloadImage (){
        String url = mangbaihat.get(position).getFields().getImage_Url().trim();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(mangbaihat.get(position).getFields().getMusic_Title());
        request.setDescription("Đang tải ảnh ....");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        long d = System.currentTimeMillis();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+d+".jpeg");

        //Get dowload service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+d+".jpeg";
    }
    private String startDowloadMp3 (){
        String url = mangbaihat.get(position).getFields().getMusic_Url().trim();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle(mangbaihat.get(position).getFields().getMusic_Title());
        request.setDescription("Đang tải nhạc ....");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        long d = System.currentTimeMillis();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+d+".mp3");

        //Get dowload service and enqueue file
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+d+".mp3";
//        System.out.println("Đây là đường dẫn file nhạc "+ Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+d+".mp3");
    }

    private void eventClick() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (adapternhac.getItem(1) != null){
                    if (mangbaihat.size() >0){
                        fragment_dia_nhac.PlayNhac(mangbaihat.get(position).getFields().getImage_Url());
                        handler.removeCallbacks(this);
                    }
                    else {
                        handler.postDelayed(this,300);
                    }
                }
            }
        },500);
        imgplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (musicService.isPlaying()){
                    musicService.pause();
                    imgplay.setImageResource(R.drawable.iconplay);
                    NotifyPause();
                }else {
                    musicService.start();
                    imgplay.setImageResource(R.drawable.iconpause);
                    TestNotify();
                }
            }
        });
        imgrepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat == false)
                {
                    if (checkrandom == true){
                        checkrandom = false;
                        imgrepeat.setImageResource(R.drawable.iconsyned);
                        imgrandom.setImageResource(R.drawable.iconsuffle);

                    }
                    imgrepeat.setImageResource(R.drawable.iconsyned);
                    repeat = true;

                }else {
                    imgrepeat.setImageResource(R.drawable.iconrepeat);
                    repeat = false;
                }
            }
        });
        imgrandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkrandom == false)
                {
                    if (repeat == true){
                        repeat = false;
                        imgrandom.setImageResource(R.drawable.iconshuffled);
                        imgrepeat.setImageResource(R.drawable.iconrepeat);

                    }
                    imgrandom.setImageResource(R.drawable.iconshuffled);
                    checkrandom = true;

                }else {
                    imgrandom.setImageResource(R.drawable.iconsuffle);
                    checkrandom = false;
                }
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
                if(musicService.mediaPlayer!=null) {
                    musicService.seekTo(seekBar.getProgress());
                }
            }
        });
        imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mangbaihat.size() > 0){
                    if (musicService.isPlaying() || musicService != null){
                        musicService.stop();
                        musicService.release();
//                        mediaPlayer = null;
                    }
                    if (position < (mangbaihat.size())){
                        imgplay.setImageResource(R.drawable.iconpause);
                        position++;
                        checkFavorite();
                        checkDowload();
                        if (repeat == true){
                            if (position == 0){
                                position = mangbaihat.size();
                            }
                            position -= 1;
                        }
                        if (checkrandom == true){
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size());
                            if (index == position){
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > (mangbaihat.size() - 1)){
                            position = 0;
                        }
                        musicService.createMediaPlayer(mangbaihat.get(position).getFields().getMusic_Url());
                        musicService.start();
                        TimeSong();
                        UpdateTime();
//                        new PlayMp3().execute(mangbaihat.get(position).getFields().getMusic_Url());
                        fragment_dia_nhac.PlayNhac(mangbaihat.get(position).getFields().getImage_Url());
                        getSupportActionBar().setTitle(mangbaihat.get(position).getFields().getMusic_Title());
                    }
                }
                imgpreview.setClickable(false);
                imgnext.setClickable(false);
                TestNotify();
                setArrMusicMini();
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgpreview.setClickable(true);
                        imgnext.setClickable(true);
                    }
                },5000);
            }
        });
        imgpreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mangbaihat.size() > 0){
                    if (musicService.isPlaying() || musicService != null){
                        musicService.stop();
                        musicService.release();
//                        mediaPlayer = null;
                    }
                    if (position < (mangbaihat.size())){
                        imgplay.setImageResource(R.drawable.iconpause);
                        position--;
                        checkFavorite();
                        checkDowload();
                        if (position < 0 ){
                            position = mangbaihat.size() - 1;
                        }
                        if (repeat == true){
                            position += 1;
                        }
                        if (checkrandom == true){
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size());
                            if (index == position){
                                position = index - 1;
                            }
                            position = index;
                        }
                        musicService.createMediaPlayer(mangbaihat.get(position).getFields().getMusic_Url());
                        musicService.start();
                        TimeSong();
                        UpdateTime();
//                        new PlayMp3().execute(mangbaihat.get(position).getFields().getMusic_Url());
                        fragment_dia_nhac.PlayNhac(mangbaihat.get(position).getFields().getImage_Url());
                        getSupportActionBar().setTitle(mangbaihat.get(position).getFields().getMusic_Title());
                    }
                }
                imgpreview.setClickable(false);
                imgnext.setClickable(false);
                TestNotify();
                setArrMusicMini();
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imgpreview.setClickable(true);
                        imgnext.setClickable(true);
                    }
                },5000);
            }
        });
        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessFavorite();
                MobileAds.initialize(Detail_Music.this, new OnInitializationCompleteListener() {
                    @Override
                    public void onInitializationComplete(InitializationStatus initializationStatus) {
                    }
                });
                AdRequest adRequest = new AdRequest.Builder()
                        .build();
                RewardedAd.load(Detail_Music.this, getResources().getString(R.string.rewarded),
                        adRequest, new RewardedAdLoadCallback() {
                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                // Handle the error.
                                mRewardedAd = null;
                            }

                            @Override
                            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                                mRewardedAd = rewardedAd;
                                if (mRewardedAd != null) {
                                    Activity activityContext = Detail_Music.this;
                                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                        @Override
                                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                            // Handle the reward.
                                            int rewardAmount = rewardItem.getAmount();
                                            String rewardType = rewardItem.getType();
                                        }
                                    });
                                } else {
                                }
                                mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when ad is shown.
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when ad fails to show.
                                    }

                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when ad is dismissed.
                                        // Set the ad reference to null so you don't show the ad a second time.
                                        mRewardedAd = null;
                                    }
                                });
                            }
                });
            }
        });
    }
    private void checkFavorite(){
        SharedPreferences pref = getSharedPreferences("PREF", MODE_PRIVATE);
        String value = pref.getString("@Favorite", null);
        if(value==null){
            status_favorite = false;
            btn_favorite.setImageResource(R.drawable.ic_favorite_border_white);
        }else{
            JsonArray jArray = new JsonParser().parse(value).getAsJsonArray();
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<Item> item_favorite = gson.fromJson(jArray,type);
            int index = findIndex(item_favorite,mangbaihat.get(position).getId());
            if(index!=-1){
                status_favorite = true;
                btn_favorite.setImageResource(R.drawable.ic_fill_favorite_white);
            }else{
                status_favorite = false;
                btn_favorite.setImageResource(R.drawable.ic_favorite_border_white);
            }
        }
    }
    private void checkDowload(){
        SharedPreferences pref = getSharedPreferences("PREF", MODE_PRIVATE);
        String value = pref.getString("@MusicDownload", null);
        if(value==null){
            status_download = false;
            btn_download.setImageResource(R.drawable.ic_arrow_downward_white);
        }else{
            JsonArray jArray = new JsonParser().parse(value).getAsJsonArray();
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<Item> item_favorite = gson.fromJson(jArray,type);
            int index = findIndex(item_favorite,mangbaihat.get(position).getId());
            if(index!=-1){
                status_download = true;
                btn_download.setImageResource(R.drawable.ic_cloud_done_white);
            }else{
                status_download = false;
                btn_download.setImageResource(R.drawable.ic_arrow_downward_white);
            }
        }
    }
    public static int findIndex(ArrayList<Item> arr, String t)
    {

        // if array is Null
        if (arr == null) {
            return -1;
        }

        // find length of array
        int len = arr.size();
        int i = 0;

        // traverse in the array
        while (i < len) {

            // if the i-th element is t
            // then return the index
            if (arr.get(i).getId().equals(t)) {
                return i;
            }
            else {
                i = i + 1;
            }
        }
        return -1;
    }
    private void ProcessFavorite (){
        if(status_favorite==false){
            addFavorite();
        }else{
            removeFavorite();
        }
    }
    private void addFavorite(){
        btn_favorite.setImageResource(R.drawable.ic_fill_favorite_white);
        SharedPreferences pref = getSharedPreferences("PREF", MODE_PRIVATE);

        //Get data string
        String value = pref.getString("@Favorite", null);
        if(value==null){
            ArrayList<Item> item_favorite = new ArrayList<>();
            item_favorite.add(mangbaihat.get(position));
            Gson gson = new Gson();
            String json = gson.toJson(item_favorite);
            pref.edit().putString("@Favorite", json).commit();
            status_favorite = true;
        }else{
            JsonArray jArray = new JsonParser().parse(value).getAsJsonArray();
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<Item> item_favorite = gson.fromJson(jArray,type);
            item_favorite.add(mangbaihat.get(position));
            String json = gson.toJson(item_favorite);
            pref.edit().putString("@Favorite", json).commit();
            status_favorite = true;
        }
        refreshFragment();
    }
    private void removeFavorite(){
        System.out.println("Lệnh hủy tym");
        btn_favorite.setImageResource(R.drawable.ic_favorite_border_white);
        SharedPreferences pref = getSharedPreferences("PREF", MODE_PRIVATE);
        //Get data string
        String value = pref.getString("@Favorite", null);
        JsonArray jArray = new JsonParser().parse(value).getAsJsonArray();
        Type type = new TypeToken<ArrayList<Item>>() {}.getType();
        Gson gson = new Gson();
        ArrayList<Item> item_favorite = gson.fromJson(jArray,type);
        if(item_favorite.size()==1){
            pref.edit().putString("@Favorite", null).commit();
            status_favorite = false;
        }else{
            int index = findIndex(item_favorite,mangbaihat.get(position).getId());
            ArrayList<Item> arr_new = new ArrayList<>();
            for(int i=0;i<item_favorite.size();i++){
                if(i!=index){
                    arr_new.add(item_favorite.get(i));
                }
            }
            String json_new = gson.toJson(arr_new);
            pref.edit().putString("@Favorite", json_new).commit();
            status_favorite = false;
        }
        refreshFragment();
    }
    private void GetDataFromIntent() {
        Intent intent = getIntent();
        mangbaihat.clear();
        if (intent != null)
        {
            if (intent.hasExtra("cakhuc")){
                Item baihat = (Item) intent.getSerializableExtra("cakhuc");
                mangbaihat.add(baihat);
            }
            if (intent.hasExtra("cacbaihat")){
                ArrayList<Item> items = (ArrayList<Item>) intent.getSerializableExtra("cacbaihat");
                mangbaihat = items;

            }
        }

    }

    private void init() {
        toolbarplaynhac = findViewById(R.id.toolbarplaynhac);
        txtTimesong = findViewById(R.id.textviewtimesong);
        txtTotaltimesong = findViewById(R.id.textviewtotaltimesong);
        sktime = findViewById(R.id.seekbarsong);
        imgplay = findViewById(R.id.imagebuttonplay);
        imgnext = findViewById(R.id.imagebuttonnext);
        imgpreview = findViewById(R.id.imagebuttonpreview);
        imgrepeat = findViewById(R.id.imagebuttonrepeat);
        imgrandom = findViewById(R.id.imagebuttonsuffle);
        viewPagerplaynhac = findViewById(R.id.viewpagerplaynhac);
        setSupportActionBar(toolbarplaynhac);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarplaynhac.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                setArrMusicMini();
            }
        });
        TestNotify();
        setArrMusicMini();
        toolbarplaynhac.setTitleTextColor(Color.WHITE);
        fragment_dia_nhac = new Fragment_Dia_Nhac();
        fragment_play_danh_sach_cac_bai_hat = new Fragment_Play_danh_sach_cac_bai_hat();
        adapternhac = new ViewPagerPlayListNhac(getSupportFragmentManager());
        adapternhac.AddFragment(fragment_dia_nhac);
        adapternhac.AddFragment(fragment_play_danh_sach_cac_bai_hat);
        viewPagerplaynhac.setAdapter(adapternhac);
        // fragment_dia_nhac = (Fragment_Dia_Nhac) adapternhac.getItem(1);
        if (mangbaihat.size() >0){
            Intent intent = getIntent();
            if(intent.hasExtra("CONTINUE")){
                if(musicService.mediaPlayer!=null){
                    getSupportActionBar().setTitle(mangbaihat.get(position).getFields().getMusic_Title());
                    TimeSong();
                    UpdateTime();
                }else{
                    getSupportActionBar().setTitle(mangbaihat.get(position).getFields().getMusic_Title());
                    musicService.createMediaPlayer(mangbaihat.get(position).getFields().getMusic_Url());
                    musicService.start();
                    TimeSong();
                    UpdateTime();
                }
            }else{
                System.out.println("Đây là bài hát"+mangbaihat.get(position));
                getSupportActionBar().setTitle(mangbaihat.get(position).getFields().getMusic_Title());
                musicService.createMediaPlayer(mangbaihat.get(position).getFields().getMusic_Url());
                musicService.start();
                TimeSong();
                UpdateTime();
            }

//            new PlayMp3().execute(mangbaihat.get(position).getFields().getMusic_Url());
            imgplay.setImageResource(R.drawable.iconpause);
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Intent intent = new Intent(this,MusicService.class);
//        bindService(intent,this,BIND_AUTO_CREATE);
//    }



//    class PlayMp3 extends AsyncTask<String,Void,String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            return strings[0];
//        }
//
//        @Override
//        protected void onPostExecute(String baihat) {
//            super.onPostExecute(baihat);
//            try {
//                mediaPlayer = new MediaPlayer();
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        mediaPlayer.stop();
//                        mediaPlayer.reset();
//                    }
//                });
//                mediaPlayer.setDataSource(baihat);
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mediaPlayer.start();
//            TimeSong();
//            UpdateTime();
//        }
//    }
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
                            next = true;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
        },300);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (next == true){
                    if (position < (mangbaihat.size())){
                        imgplay.setImageResource(R.drawable.iconpause);
                        position++;
                        checkFavorite();
                        checkDowload();
                        if (repeat == true){
                            if (position == 0){
                                position = mangbaihat.size();
                            }
                            position -= 1;
                        }
                        if (checkrandom == true){
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size());
                            if (index == position){
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > (mangbaihat.size() - 1)){
                            position = 0;
                        }
                        musicService.createMediaPlayer(mangbaihat.get(position).getFields().getMusic_Url());
                        musicService.start();
                        TimeSong();
                        UpdateTime();
//                        new PlayMp3().execute(mangbaihat.get(position).getFields().getMusic_Url());
                        fragment_dia_nhac.PlayNhac(mangbaihat.get(position).getFields().getImage_Url());
                        getSupportActionBar().setTitle(mangbaihat.get(position).getFields().getMusic_Title());
                    }
                    imgpreview.setClickable(false);
                    imgnext.setClickable(false);
                    TestNotify();
                    setArrMusicMini();
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgpreview.setClickable(true);
                            imgnext.setClickable(true);
                        }
                    },5000);
                    next = false;
                    handler1.removeCallbacks(this,1000);
                }else {
                    handler1.postDelayed(this,1000);
                }
            }
        },1000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
            setArrMusicMini();
            return false; //I have tried here true also
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setArrMusicMini(){
        ArrayList<Item> item_to_add = new ArrayList<>();
        item_to_add = mangbaihat;
        SharedPreferences pref = getSharedPreferences("PREF", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(item_to_add);
        pref.edit().putString("@Arr_Mini", json).commit();
        pref.edit().putString("@Position_Music_Bottom", String.valueOf(position)).commit();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_STORAGE_CODE:
                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_DENIED){
                    startDowload();
                }else{
                    Toast.makeText(this,"Bây giờ, bạn có thể tải nhạc ...",Toast.LENGTH_SHORT).show();
                }
        }
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Intent prevIntent = new Intent(this,NotificationReceiver.class).setAction("ActionPrev");
        Intent pauseIntent = new Intent(this,NotificationReceiver.class).setAction("ActionPause");
        Intent nextIntent = new Intent(this,NotificationReceiver.class).setAction("ActionNext");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        PendingIntent prevpendingIntent = PendingIntent.getBroadcast(this,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pausependingIntent = PendingIntent.getBroadcast(this,0,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent nextpendingIntent = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(mangbaihat.get(position).getFields().getImage_Url().contains("/storage/emulated/0/Download")){
            final BitmapFactory.Options options = new BitmapFactory.Options();
            picture = BitmapFactory.decodeFile(mangbaihat.get(position).getFields().getImage_Url(), options);
        }else{
            picture = getBitmapFromURL(mangbaihat.get(position).getFields().getImage_Url());
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(picture)
                .setContentTitle(mangbaihat.get(position).getFields().getMusic_Title())
                .setContentText(mangbaihat.get(position).getFields().getComposed())
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
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

    private void NotifyPause(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Intent prevIntent = new Intent(this,NotificationReceiver.class).setAction("ActionPrev");
        Intent playIntent = new Intent(this, NotificationReceiver.class).setAction("ActionPlay");
        Intent nextIntent = new Intent(this,NotificationReceiver.class).setAction("ActionNext");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        PendingIntent prevpendingIntent = PendingIntent.getBroadcast(this,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent nextpendingIntent = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent playpendingIntent = PendingIntent.getBroadcast(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(mangbaihat.get(position).getFields().getImage_Url().contains("/storage/emulated/0/Download")){
            final BitmapFactory.Options options = new BitmapFactory.Options();
            picture = BitmapFactory.decodeFile(mangbaihat.get(position).getFields().getImage_Url(), options);
        }else{
            picture = getBitmapFromURL(mangbaihat.get(position).getFields().getImage_Url());
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(picture)
                .setContentTitle(mangbaihat.get(position).getFields().getMusic_Title())
                .setContentText(mangbaihat.get(position).getFields().getComposed())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.iconpreview_mini,"Previous",prevpendingIntent)
                .addAction(R.drawable.iconplay_mini,"Play",playpendingIntent)
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
        notificationManager.notify(0, builder.build());
    }

    @Override
    public void nextClick() {
        if (mangbaihat.size() > 0){
            if (musicService.isPlaying() || musicService != null){
                musicService.stop();
                musicService.release();
//                        mediaPlayer = null;
            }
            if (position < (mangbaihat.size())){
                imgplay.setImageResource(R.drawable.iconpause);
                position++;
                checkFavorite();
                checkDowload();
                if (repeat == true){
                    if (position == 0){
                        position = mangbaihat.size();
                    }
                    position -= 1;
                }
                if (checkrandom == true){
                    Random random = new Random();
                    int index = random.nextInt(mangbaihat.size());
                    if (index == position){
                        position = index - 1;
                    }
                    position = index;
                }
                if (position > (mangbaihat.size() - 1)){
                    position = 0;
                }
                musicService.createMediaPlayer(mangbaihat.get(position).getFields().getMusic_Url());
                musicService.start();
                TimeSong();
                UpdateTime();
//                        new PlayMp3().execute(mangbaihat.get(position).getFields().getMusic_Url());
                fragment_dia_nhac.PlayNhac(mangbaihat.get(position).getFields().getImage_Url());
                getSupportActionBar().setTitle(mangbaihat.get(position).getFields().getMusic_Title());
            }
        }
        imgpreview.setClickable(false);
        imgnext.setClickable(false);
        TestNotify();
        setArrMusicMini();
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgpreview.setClickable(true);
                imgnext.setClickable(true);
            }
        },5000);
    }

    @Override
    public void prevClick() {
        if (mangbaihat.size() > 0){
            if (musicService.isPlaying() || musicService != null){
                musicService.stop();
                musicService.release();
//                        mediaPlayer = null;
            }
            if (position < (mangbaihat.size())){
                imgplay.setImageResource(R.drawable.iconpause);
                position--;
                checkFavorite();
                checkDowload();
                if (position < 0 ){
                    position = mangbaihat.size() - 1;
                }
                if (repeat == true){
                    position += 1;
                }
                if (checkrandom == true){
                    Random random = new Random();
                    int index = random.nextInt(mangbaihat.size());
                    if (index == position){
                        position = index - 1;
                    }
                    position = index;
                }
                musicService.createMediaPlayer(mangbaihat.get(position).getFields().getMusic_Url());
                musicService.start();
                TimeSong();
                UpdateTime();
//                        new PlayMp3().execute(mangbaihat.get(position).getFields().getMusic_Url());
                fragment_dia_nhac.PlayNhac(mangbaihat.get(position).getFields().getImage_Url());
                getSupportActionBar().setTitle(mangbaihat.get(position).getFields().getMusic_Title());
            }
        }
        imgpreview.setClickable(false);
        imgnext.setClickable(false);
        TestNotify();
        setArrMusicMini();
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgpreview.setClickable(true);
                imgnext.setClickable(true);
            }
        },5000);
    }

    @Override
    public void playClick() {
        if (musicService.isPlaying()){
            musicService.pause();
            imgplay.setImageResource(R.drawable.iconplay);
            NotifyPause();
        }else {
            musicService.start();
            imgplay.setImageResource(R.drawable.iconpause);
            TestNotify();
        }
    }

    private void refreshFragment(){
        FragmentNew f = (FragmentNew) FragmentNew.myFragment;
        f.getFragmentManager().findFragmentByTag("2");
        if (f!= null) {
            f.refreshFragment();
        }
        FragmentFavorite f2 = (FragmentFavorite) FragmentFavorite.myFragment;
        f.getFragmentManager().findFragmentByTag("2");
        if (f2!= null) {
            f2.refreshFragment();
        }
        FragmentDowload f3 = (FragmentDowload) FragmentDowload.myFragment;
        f.getFragmentManager().findFragmentByTag("3");
        if (f3!= null) {
            f3.refreshFragment();
        }
        Detail_Category detail_category = (Detail_Category) Detail_Category.myActivity;
        if(detail_category!=null){
            detail_category.refresh();
        }
    }
}

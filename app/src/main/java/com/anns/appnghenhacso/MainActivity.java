package com.anns.appnghenhacso;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.anns.appnghenhacso.Fragment.*;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    Fragment fragment_bottom;
    private InterstitialAd mInterstitialAd;
    final Fragment fragment1 = new FragmentHome();
    final Fragment fragment2 = new FragmentNew();
    final Fragment fragment3 = new FragmentFavorite();
    final Fragment fragment4 = new FragmentDowload();
    Fragment active = fragment2;
    final FragmentManager fm = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.frament_container, new FragmentNew()).commit();

        fm.beginTransaction().add(R.id.frament_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.frament_container, fragment2, "2").commit();
        fm.beginTransaction().add(R.id.frament_container,fragment1, "1").hide(fragment1).commit();
        fm.beginTransaction().add(R.id.frament_container,fragment4, "4").hide(fragment4).commit();

        Menu menu = bottomNav.getMenu();
        menu.getItem(1).setChecked(true);

        fragment_bottom = new FragmentBottomMusic();
        fm.beginTransaction().add(R.id.frame_bottom,fragment_bottom);



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedfragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_home :
                            fm.beginTransaction().hide(active).show(fragment1).commit();
                            active = fragment1;
                            return true;
//                            selectedfragment = new FragmentHome();
//                            break;
                        case R.id.nav_new:
                            fm.beginTransaction().hide(active).show(fragment2).commit();
                            active = fragment2;
                            return true;
//                            selectedfragment = new FragmentNew();
//                            break;
                        case R.id.nav_favorites :
                            fm.beginTransaction().hide(active).show(fragment3).commit();
                            active = fragment3;
                            return true;
//                            selectedfragment = new FragmentFavorite();
//                            break;
                        case R.id.nav_dowload:
                            fm.beginTransaction().hide(active).show(fragment4).commit();
                            active = fragment4;
                            return true;
//                            selectedfragment = new FragmentDowload();
//                            break;
                    }
                    return false;
//                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
////                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
//                    transaction.replace(R.id.frament_container,selectedfragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                    return true;
                }
            };
    @Override
    protected void onResume() {
        super.onResume();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentBottomMusic fragment_bottom = new FragmentBottomMusic();
        transaction.replace(R.id.frame_bottom,fragment_bottom);
        transaction.commit();
    }

}
package com.anns.appnghenhacso.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.anns.appnghenhacso.Adapter.AdapterMucsicDowload;
import com.anns.appnghenhacso.Adapter.ListMusicCategoryAdapter;
import com.anns.appnghenhacso.Model.Item;
import com.anns.appnghenhacso.R;
import com.anns.appnghenhacso.Screen.Purchase;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;

public class FragmentDowload extends Fragment {
    RecyclerView rcl_music_download;
    private AdView mAdView;
    Button upgrade_account;
    public static Fragment myFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentdowload, container, false);
        rcl_music_download = (RecyclerView) view.findViewById(R.id.rcl_music_download);
        upgrade_account = (Button) view.findViewById(R.id.upgrade_account);
        upgrade_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Purchase.class);
                startActivity(intent);
            }
        });
        getData();

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = view.findViewById(R.id.adsdownload);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
        myFragment = this;
        return  view;
    }
    private void getData(){
        SharedPreferences pref = this.getActivity().getSharedPreferences("PREF", MODE_PRIVATE);
        String value = pref.getString("@MusicDownload", null);
        if(value==null){

        }else{
            JsonArray jArray = new JsonParser().parse(value).getAsJsonArray();
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<Item> item_favorite = gson.fromJson(jArray,type);
            Collections.sort(item_favorite, new Comparator<Item>() {
                @Override
                public int compare(Item o1, Item o2) {
                    try {
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        return formatter.parse(o2.getFields().getMusic_Size()).compareTo(formatter.parse(o1.getFields().getMusic_Size()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    };
                    return 0;
                }
            });
            AdapterMucsicDowload adapterMucsicDowload = new AdapterMucsicDowload(item_favorite,getContext());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            rcl_music_download.setLayoutManager(linearLayoutManager);
            rcl_music_download.setAdapter(adapterMucsicDowload);
            String json = gson.toJson(item_favorite);
            System.out.println("Mảng đang có " + json);
        }
    }
    public void refreshFragment() {
        System.out.println("Vô nào");
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).commitAllowingStateLoss();
        getActivity().getSupportFragmentManager().beginTransaction().attach(this).commitAllowingStateLoss();
        //adapter.notifyDataSetChanged();
    }
}

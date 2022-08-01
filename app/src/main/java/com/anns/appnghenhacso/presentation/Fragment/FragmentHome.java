package com.anns.appnghenhacso.presentation.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import com.anns.appnghenhacso.data.remote.API.APIService;
import com.anns.appnghenhacso.data.remote.MainApi;
import com.anns.appnghenhacso.presentation.Adapter.CategoryAdapter;
import com.anns.appnghenhacso.presentation.Adapter.ListNewAdapter;
import com.anns.appnghenhacso.presentation.Adapter.SliderAdapter;
import com.anns.appnghenhacso.domain.model.Model.Item;
import com.anns.appnghenhacso.domain.model.Model.ListMusic;
import com.anns.appnghenhacso.domain.model.Model.Records;
import com.anns.appnghenhacso.R;
import com.anns.appnghenhacso.presentation.Screen.Purchase;
import com.anns.appnghenhacso.presentation.Screen.Search_Result;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FragmentHome extends Fragment {
    ViewPager viewPager;
    RecyclerView rcl_music_new,rcl_category;
    ArrayList<ListMusic> listMusics;
    SearchView mSearchView;
    String TAG = "FragmentHome";
    int currentPageCuntr = 0;
    int image[] = {R.drawable.ba3,R.drawable.ba1,R.drawable.ba2};
    private AdView mAdView;
    Button btn_upgrade;
    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmenthome, container, false);
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);


        viewPager = (ViewPager)  view.findViewById(R.id.viewpager);
        rcl_music_new = (RecyclerView) view.findViewById(R.id.rcl_music_new);
        rcl_category = (RecyclerView) view.findViewById(R.id.rcl_category);
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        btn_upgrade = (Button) view.findViewById(R.id.btn_upgradehome);
        btn_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Purchase.class);
                startActivity(intent);
            }
        });

        mSearchView.setQueryHint("Tìm kiếm bài hát...");
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rcl_music_new.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 2);
        rcl_category.setLayoutManager(mLayoutManager);

        Slider();
        GetDataNew();
        GetListMusic();
        Search();
        return  view;
    }
    private void GetDataNew(){
        MainApi dataservice = APIService.getService();
        Call<Records> callback = dataservice.GetList("keyjd4mHxykM7LHvW");
        callback.enqueue(new Callback<Records>() {
            @Override
            public void onResponse(Call<Records> call, Response<Records> response) {
                Records record_new = response.body();
                ArrayList<Item> item = record_new.getItems();
                Collections.sort(item, new Comparator<Item>() {
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
                ListNewAdapter adapter = new ListNewAdapter(item,getContext());
                rcl_music_new.setAdapter(adapter);

                Gson gson = new Gson();
                String json = gson.toJson(record_new.getItems());
                System.out.println("This is dataa"+json);
            }

            @Override
            public void onFailure(Call<Records> call, Throwable t) {

            }
        });
    }

    private void Slider(){
        viewPager.setAdapter(new SliderAdapter(image,getActivity()));
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPageCuntr == image.length){
                    currentPageCuntr = 0;

                }
                viewPager.setCurrentItem(currentPageCuntr++,true);
            }
        };
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        },4500,4500);
    }

    private void GetListMusic(){

        listMusics = new ArrayList<>();
        ListMusic listMusic1 = new ListMusic("appkIMo6DC6fqO2KK",R.drawable.category_1,"TOP 100");
        ListMusic listMusic8 = new ListMusic("app0hEtQ8jNQVUOdq",R.drawable.category_8,"Mới Cập Nhật");
        ListMusic listMusic2 = new ListMusic("apptUNc22698zH0kc",R.drawable.category_2,"Nhạc TikTok");
        ListMusic listMusic3 = new ListMusic("appxs2hdYOQfcPBPH",R.drawable.category_3,"Nhạc Việt Mix");
        ListMusic listMusic4 = new ListMusic("applbCKxXZtNQbVhe",R.drawable.category_4,"Nhạc EDM House");
        ListMusic listMusic5 = new ListMusic("apprd4UMUb8ZbTji9",R.drawable.category_5,"Nhạc Remix Việt");
        ListMusic listMusic6 = new ListMusic("appQQEixWICKKbrFd",R.drawable.category_6,"Nhạc Nonstop");
        ListMusic listMusic7 = new ListMusic("appImLGhSkmSgxzyt",R.drawable.category_7,"China DJ Mix");

        listMusics.add(listMusic1);
        listMusics.add(listMusic8);
        listMusics.add(listMusic2);
        listMusics.add(listMusic3);
        listMusics.add(listMusic4);
        listMusics.add(listMusic5);
        listMusics.add(listMusic6);
        listMusics.add(listMusic7);

        CategoryAdapter categoryAdapter = new CategoryAdapter(listMusics,getContext());
        rcl_category.setAdapter(categoryAdapter);

    }

    private void Search(){
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getActivity(), Search_Result.class);
                intent.putExtra("VALUE_SEARCH",query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}

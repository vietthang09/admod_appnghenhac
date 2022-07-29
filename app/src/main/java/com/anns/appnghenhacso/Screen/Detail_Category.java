package com.anns.appnghenhacso.Screen;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.anns.appnghenhacso.API.APIService;
import com.anns.appnghenhacso.API.Dataservice;
import com.anns.appnghenhacso.Adapter.ListMusicCategoryAdapter;
import com.anns.appnghenhacso.Fragment.FragmentBottomMusic;
import com.anns.appnghenhacso.Model.Item;
import com.anns.appnghenhacso.Model.Records;
import com.anns.appnghenhacso.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Detail_Category extends AppCompatActivity {
    private  String id;
    private  String title;
    private  int image;
    private TextView detail_name_list;
    private ImageView detail_image_list;
    private RecyclerView rcl_music_category;
    private ImageButton back_from_category;
    private AdView mAdView;
    LinearLayout loading_cate;
    public static Activity myActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__category);

        myActivity = this;
        Intent intent = getIntent();
        id = intent.getStringExtra("ID_LIST");
        title = intent.getStringExtra("Title_LIST");
        image = intent.getIntExtra("Image_LIST",0);

        detail_name_list = (TextView) findViewById(R.id.detail_name_list);
        rcl_music_category = (RecyclerView) findViewById(R.id.rcl_music_category);
        back_from_category = (ImageButton) findViewById(R.id.back_from_category);
        loading_cate = findViewById(R.id.loading_cate);
        back_from_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentBottomMusic fragment_bottom = new FragmentBottomMusic();
        transaction.add(R.id.bottom_music_cate,fragment_bottom);
        transaction.commit();

//        Picasso.get().load(image).into(detail_image_list);
        detail_name_list.setText(title);

        MobileAds.initialize(Detail_Category.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adscategory);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        getData();
    }
    private void getData(){
        Dataservice dataservice = APIService.getService();
        Call<Records> callback = dataservice.GetMusicByCategory(id,"keyjd4mHxykM7LHvW");
        callback.enqueue(new Callback<Records>() {
            @Override
            public void onResponse(Call<Records> call, Response<Records> response) {
                loading_cate.setVisibility(View.INVISIBLE);
                Records record = response.body();
                ArrayList<Item> item = record.getItems();
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
                ListMusicCategoryAdapter listMusicCategoryAdapter = new ListMusicCategoryAdapter(item,getApplicationContext());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                rcl_music_category.setLayoutManager(linearLayoutManager);
                rcl_music_category.setAdapter(listMusicCategoryAdapter);

            }

            @Override
            public void onFailure(Call<Records> call, Throwable t) {

            }
        });
    }

    public void refresh(){
        this.recreate();
    }
    @Override
    protected void onResume() {
        super.onResume();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        FragmentBottomMusic fragment_bottom = new FragmentBottomMusic();
        transaction.add(R.id.bottom_music_cate,fragment_bottom);
        transaction.commit();
    }

    private Date convertDate(String d) throws ParseException {
        return new SimpleDateFormat("dd/MM/yyyy").parse(d);
    }
}
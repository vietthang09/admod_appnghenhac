package com.anns.appnghenhacso.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.anns.appnghenhacso.API.APIService;
import com.anns.appnghenhacso.API.Dataservice;
import com.anns.appnghenhacso.Adapter.ListMusicCategoryAdapter;
import com.anns.appnghenhacso.Model.Item;
import com.anns.appnghenhacso.Model.Records;
import com.anns.appnghenhacso.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FragmentNew extends Fragment {
    private RecyclerView rcl_music_category;
    private AdView mAdView;
    LinearLayout container_detail_new;
    LinearLayout loading_new;
    ArrayList<Item> new_arr;
    ArrayList<Item> item;
    private boolean isLoading = false;
    ListMusicCategoryAdapter listMusicCategoryAdapter;
    public  static  Fragment myFragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, container, false);

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        myFragment = this;
        mAdView = view.findViewById(R.id.adsnew);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);

        container_detail_new = view.findViewById(R.id.container_detail_new);
        loading_new = view.findViewById(R.id.loading_new);
        container_detail_new.setVisibility(View.INVISIBLE);
        rcl_music_category = view.findViewById(R.id.rcl_music_category_new);

        getData();
        initScrollListener();
        return  view;

    }

    private void getData(){
        Dataservice dataservice = APIService.getService();
        Call<Records> callback = dataservice.GetMusicByCategory("app0hEtQ8jNQVUOdq","keyjd4mHxykM7LHvW");
        callback.enqueue(new Callback<Records>() {
            @Override
            public void onResponse(Call<Records> call, Response<Records> response) {
                container_detail_new.setVisibility(View.VISIBLE);
                loading_new.setVisibility(View.INVISIBLE);
                Records record = response.body();
                item = record.getItems();
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
                new_arr = new ArrayList<>();
                int i;
                for(i=0;i<10;i++){
                    new_arr.add(item.get(i));
                }
                listMusicCategoryAdapter = new ListMusicCategoryAdapter(new_arr,getContext());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                rcl_music_category.setLayoutManager(linearLayoutManager);
                rcl_music_category.setAdapter(listMusicCategoryAdapter);
            }

            @Override
            public void onFailure(Call<Records> call, Throwable t) {

            }
        });
    }
    private void initScrollListener() {
        rcl_music_category.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastCompletelyVisibleItemPosition = 0;
                lastCompletelyVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();

                if (lastCompletelyVisibleItemPosition == new_arr.size() - 1) {
                    if (!isLoading) {
                        System.out.println("LoadMore");
                        isLoading = true;
//                            pageNumber++;
                        loadMore();
                    }
                }
                //                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                if (!isLoading) {
//                    //Nếu item cuối cùng của layout = với giá trị cuối của recycleView thì ta gọi hàm LoadMore
//
//                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == new_arr.size() - 1) {
//                        //bottom of list!
//                        System.out.println("loadMore"+linearLayoutManager.findLastCompletelyVisibleItemPosition());
////                        loadMore();
//                        isLoading = true;
//                    }
//                }
            }
        });

    }
    private void loadMore() {
//                listMusicCategoryAdapter.notifyItemInserted(new_arr.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                        new_arr.remove(new_arr.size() - 1);
                int scrollPosition = new_arr.size();
                listMusicCategoryAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;
                if(nextLimit>item.size()-1){
                    nextLimit=item.size()-1;
                }

                while (currentSize - 1 < nextLimit) {
                    new_arr.add(item.get(currentSize));
                    currentSize++;
                }

                listMusicCategoryAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }
    public void refreshFragment() {
        System.out.println("Vô nào");
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).commitAllowingStateLoss();
        getActivity().getSupportFragmentManager().beginTransaction().attach(this).commitAllowingStateLoss();
        //adapter.notifyDataSetChanged();
    }
}

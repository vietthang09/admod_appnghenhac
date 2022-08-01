package com.anns.appnghenhacso.presentation.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.anns.appnghenhacso.presentation.Adapter.AdapterMusicFavorite;
import com.anns.appnghenhacso.domain.model.Model.Item;
import com.anns.appnghenhacso.R;
import com.google.android.gms.ads.*;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardedAd;
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

public class FragmentFavorite extends Fragment {
    RecyclerView rcl_favorite;
    private NativeAdView nativeAd;
    FrameLayout frameLayout;
    MediaView mediaView;
    private AdView adsfavorite;
    private  RewardedAd mRewardedAd;
    public static Fragment myFragment;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentfavorite, container, false);
        rcl_favorite = (RecyclerView) view.findViewById(R.id.rcl_favorite);
        frameLayout = view.findViewById(R.id.fl_adplaceholder);
        getDataFavorite();

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        adsfavorite = view.findViewById(R.id.adsfavorite);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adsfavorite.loadAd(adRequest);
        myFragment = this;

//        AdLoader.Builder builder = new AdLoader.Builder(getActivity(), getActivity().getResources().getString(R.string.navtive_Advanced))
//                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
//                    @Override
//                    public void onNativeAdLoaded(NativeAd nativeAd) {
//
//                        NativeAdView adView = (NativeAdView) getLayoutInflater()
//                                .inflate(R.layout.ad_unified, null);
//
//                        populateNativeAdView(nativeAd, adView);
//                        frameLayout.removeAllViews();
//                        frameLayout.addView(adView);
//                    }
//                });
//
//        AdLoader adLoader =
//                builder
//                        .withAdListener(
//                                new AdListener() {
//                                    @Override
//                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
//                                        String error =
//                                                String.format(
//                                                        "domain: %s, code: %d, message: %s",
//                                                        loadAdError.getDomain(),
//                                                        loadAdError.getCode(),
//                                                        loadAdError.getMessage());
//                                        Toast.makeText(
//                                                getActivity(),
//                                                "Failed to load native ad with error " + error,
//                                                Toast.LENGTH_SHORT)
//                                                .show();
//                                    }
//                                })
//                        .build();
//
//        adLoader.loadAd(new AdRequest.Builder().build());
        
        return  view;
    }

//    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
//        // Set the media view.
//        mediaView = adView.findViewById(R.id.ad_media);
//        adView.setMediaView(mediaView);
//        mediaView.setMediaContent(nativeAd.getMediaContent());
//
//        // Set other ad assets.
//        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
//        adView.setBodyView(adView.findViewById(R.id.ad_body));
//        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
//        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
//        adView.setPriceView(adView.findViewById(R.id.ad_price));
//        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
//        adView.setStoreView(adView.findViewById(R.id.ad_store));
//        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
//
//
//        if (nativeAd.getBody() == null) {
//            adView.getBodyView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getBodyView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
//        }
//
//        if (nativeAd.getCallToAction() == null) {
//            adView.getCallToActionView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getCallToActionView().setVisibility(View.VISIBLE);
//            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
//        }
//
//        if (nativeAd.getIcon() == null) {
//            adView.getIconView().setVisibility(View.GONE);
//        } else {
//            ((ImageView) adView.getIconView()).setImageDrawable(
//                    nativeAd.getIcon().getDrawable());
//            adView.getIconView().setVisibility(View.VISIBLE);
//        }
//
//        if (nativeAd.getPrice() == null) {
//            adView.getPriceView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getPriceView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
//        }
//
//        if (nativeAd.getStore() == null) {
//            adView.getStoreView().setVisibility(View.INVISIBLE);
//        } else {
//            adView.getStoreView().setVisibility(View.VISIBLE);
//            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
//        }
//
//        if (nativeAd.getStarRating() == null) {
//            adView.getStarRatingView().setVisibility(View.INVISIBLE);
//        } else {
//            ((RatingBar) adView.getStarRatingView())
//                    .setRating(nativeAd.getStarRating().floatValue());
//            adView.getStarRatingView().setVisibility(View.VISIBLE);
//        }
//
//        if (nativeAd.getAdvertiser() == null) {
//            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
//        } else {
//            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
//            adView.getAdvertiserView().setVisibility(View.VISIBLE);
//        }
//
//        adView.setNativeAd(nativeAd);
//
//        VideoController vc = nativeAd.getMediaContent().getVideoController();
//
//
//    }

    private void getDataFavorite(){
        SharedPreferences pref = this.getActivity().getSharedPreferences("PREF", MODE_PRIVATE);
        String value = pref.getString("@Favorite", null);
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
//            ListMusicCategoryAdapter listMusicCategoryAdapter = new ListMusicCategoryAdapter(item_favorite,getContext());
            AdapterMusicFavorite adapterMusicFavorite = new AdapterMusicFavorite(item_favorite,getContext());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            rcl_favorite.setLayoutManager(linearLayoutManager);
            rcl_favorite.setAdapter(adapterMusicFavorite);
        }
    }
    public void refreshFragment() {
        System.out.println("Vô nào");
        getActivity().getSupportFragmentManager().beginTransaction().detach(this).commitAllowingStateLoss();
        getActivity().getSupportFragmentManager().beginTransaction().attach(this).commitAllowingStateLoss();
        //adapter.notifyDataSetChanged();
    }
}

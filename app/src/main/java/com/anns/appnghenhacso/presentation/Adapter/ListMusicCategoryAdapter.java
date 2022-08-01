package com.anns.appnghenhacso.presentation.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anns.appnghenhacso.presentation.Fragment.FragmentFavorite;
import com.anns.appnghenhacso.domain.model.Model.Item;
import com.anns.appnghenhacso.R;
import com.anns.appnghenhacso.presentation.Screen.Detail_Music;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

public class ListMusicCategoryAdapter extends RecyclerView.Adapter<ListMusicCategoryAdapter.ViewHolder> {
    ArrayList<Item> items;
    Context context;
    boolean status_favorite;
    int time = 0;
    private RewardedAd mRewardedAd;
    public ListMusicCategoryAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ListMusicCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list_category,parent,false);
        return new ListMusicCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  ListMusicCategoryAdapter.ViewHolder holder, int position) {
            checkFavorite(holder.btn_favorite_list,position);
            String s = items.get(position).getFields().getMusic_Title();
            String title =  truncate(s);
            holder.title_music.setText(title);
            holder.text_luotnghe.setText(items.get(position).getFields().getLuot_nghe());
            holder.text_luotthich.setText(items.get(position).getFields().getLuot_thich());
            holder.compose_music.setText(items.get(position).getFields().getComposed());
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        String strDate = formatter.format(items.get(position).getFields().getMusic_Size());
            holder.ngay_phat_hanh.setText(items.get(position).getFields().getMusic_Size());
            Picasso.get().load(items.get(position).getFields().getImage_Url()).into(holder.list_category_image);
            holder.card_music_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Detail_Music.class);

                    intent.putExtra("cacbaihat",items);
                    intent.putExtra("index",position);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });

            holder.btn_favorite_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProcessFavorite(holder.btn_favorite_list,position);
                }
            });

    }
    private String truncate (String text){
        if(text.length()>50){
            return text.substring(0, 47) + "...";
        }else{
            return text;
        }
    }
    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ArrayList<Item> arr_new = new ArrayList<>();
        if (charText.length() == 0) {
            arr_new.addAll(items);
        } else {
            for(int i=0;i<items.size();i++){
                if(items.get(i).getFields().getMusic_Title().toLowerCase(Locale.getDefault()).contains(charText)){
                    arr_new.add(items.get(i));
                }
            }
        }
        items = arr_new;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_music,compose_music,text_luotnghe,text_luotthich,ngay_phat_hanh;
        ImageView list_category_image;
        CardView card_music_category;
        ImageButton btn_favorite_list;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_music = (TextView) itemView.findViewById(R.id.title_music);
            compose_music = (TextView) itemView.findViewById(R.id.compose_music);
            list_category_image = (ImageView) itemView.findViewById(R.id.list_category_image);
            card_music_category = (CardView) itemView.findViewById(R.id.card_music_category);
            text_luotnghe = (TextView) itemView.findViewById(R.id.text_luotnghe);
            text_luotthich = (TextView) itemView.findViewById(R.id.text_luotthich);
            ngay_phat_hanh = (TextView) itemView.findViewById(R.id.ngay_phat_hanh);
            btn_favorite_list = (ImageButton) itemView.findViewById(R.id.btn_favorite_list);
        }
    }
    private void checkFavorite(ImageButton btn_favorite, int position){
        SharedPreferences pref = context.getSharedPreferences("PREF", MODE_PRIVATE);
        String value = pref.getString("@Favorite", null);
        if(value==null){
            status_favorite = false;
            btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_black);
        }else{
            JsonArray jArray = new JsonParser().parse(value).getAsJsonArray();
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<Item> item_favorite = gson.fromJson(jArray,type);
            int index = findIndex(item_favorite,items.get(position).getId());
            if(index!=-1){
                status_favorite = true;
                btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_black);
            }else{
                status_favorite = false;
                btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_black);
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

    private void ProcessFavorite (ImageButton btn_favorite, int position){
        if(time==0){
            showAds();
        }else{

        }
        checkFavorite(btn_favorite,position);
        if(status_favorite==false){
            addFavorite(btn_favorite,position);
        }else{
            removeFavorite(btn_favorite,position);
        }
    }
    private void removeFavorite(ImageButton btn_favorite, int position){
        System.out.println("Lệnh hủy tym");
        btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_black);
        SharedPreferences pref = context.getSharedPreferences("PREF", MODE_PRIVATE);
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
            int index = findIndex(item_favorite,items.get(position).getId());
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
        FragmentFavorite f = (FragmentFavorite) FragmentFavorite.myFragment;
        f.getFragmentManager().findFragmentByTag("2");
        if (f!= null) {
            f.refreshFragment();
        }
    }

    private void addFavorite(ImageButton btn_favorite, int position){
        btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_black);
        SharedPreferences pref = context.getSharedPreferences("PREF", MODE_PRIVATE);

        //Get data string
        String value = pref.getString("@Favorite", null);
        if(value==null){
            ArrayList<Item> item_favorite = new ArrayList<>();
            item_favorite.add(items.get(position));
            Gson gson = new Gson();
            String json = gson.toJson(item_favorite);
            pref.edit().putString("@Favorite", json).commit();
            status_favorite = true;
        }else{
            JsonArray jArray = new JsonParser().parse(value).getAsJsonArray();
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<Item> item_favorite = gson.fromJson(jArray,type);
            item_favorite.add(items.get(position));
            String json = gson.toJson(item_favorite);
            pref.edit().putString("@Favorite", json).commit();
            status_favorite = true;
        }
        FragmentFavorite f = (FragmentFavorite) FragmentFavorite.myFragment;
        f.getFragmentManager().findFragmentByTag("2");
        if (f!= null) {
            f.refreshFragment();
        }
    }
    public void reverseTimer(int Seconds) {

        new CountDownTimer(Seconds * 1000 + 1000, 1000) {

            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);

                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);
                time = 1;
            }

            public void onFinish() {
                System.out.println("finish");
                time = 0;
            }
        }.start();
    }
    private void showAds(){
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        RewardedAd.load(context, context.getResources().getString(R.string.rewarded),
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
                            Activity activityContext = (Activity) context;
                            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    // Handle the reward.
                                    int rewardAmount = rewardItem.getAmount();
                                    String rewardType = rewardItem.getType();
                                }
                            });
                            reverseTimer(240);
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
//    @Override
//    public int getItemViewType(int position) {
//        return items.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
//    }
}

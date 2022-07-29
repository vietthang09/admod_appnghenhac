package com.anns.appnghenhacso.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anns.appnghenhacso.ModelYT.ItemYT;
import com.anns.appnghenhacso.R;
import com.anns.appnghenhacso.Screen.Detail_Music;
import com.anns.appnghenhacso.Screen.VideoViewYT;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterListYoutube extends RecyclerView.Adapter<AdapterListYoutube.ViewHolder> {
    ArrayList<ItemYT> items;
    Context context;

    public AdapterListYoutube(ArrayList<ItemYT> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list_yt,parent,false);
        return new AdapterListYoutube.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.ownerChannelText.setText(items.get(position).getOwnerChannelText());
        holder.publishedTimeText.setText(items.get(position).getPublishedTimeText());
        holder.viewCountText.setText(String.valueOf(items.get(position).getViewCountText()));
        holder.lengthTextSimpleText.setText(items.get(position).getLengthTextSimpleText());

        Picasso.get().load(items.get(position).getThumbnail()).into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideoViewYT.class);
                intent.putExtra("idVideo",items.get(position).getId());
                intent.putExtra("cacbaihat",items);
                intent.putExtra("index",position);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title,ownerChannelText,publishedTimeText,viewCountText,lengthTextSimpleText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title);
            ownerChannelText = (TextView) itemView.findViewById(R.id.ownerChannelText);
            publishedTimeText = (TextView) itemView.findViewById(R.id.publishedTimeText);
            viewCountText = (TextView) itemView.findViewById(R.id.viewCountText);
            lengthTextSimpleText = (TextView) itemView.findViewById(R.id.lengthTextSimpleText);
        }
    }
}

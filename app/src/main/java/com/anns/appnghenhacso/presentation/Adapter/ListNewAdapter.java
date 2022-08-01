package com.anns.appnghenhacso.presentation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anns.appnghenhacso.domain.model.Model.Item;
import com.anns.appnghenhacso.R;
import com.anns.appnghenhacso.presentation.Screen.Detail_Music;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListNewAdapter extends RecyclerView.Adapter<ListNewAdapter.ViewHolder> {
    ArrayList<Item> items;
    Context context;

    public ListNewAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ListNewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list_new,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListNewAdapter.ViewHolder holder, int position) {
        Picasso.get().load(items.get(position).getFields().getImage_Url()).into(holder.anhmusic);
        holder.btn_play_music_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Detail_Music.class);

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

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView anhmusic;
        Button btn_play_music_new;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            anhmusic = (ImageView)itemView.findViewById(R.id.image_rcl_new);
            btn_play_music_new = (Button) itemView.findViewById(R.id.btn_play_music_new);
        }
    }
}

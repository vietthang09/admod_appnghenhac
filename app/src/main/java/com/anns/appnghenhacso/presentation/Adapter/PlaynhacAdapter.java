package com.anns.appnghenhacso.presentation.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anns.appnghenhacso.domain.model.Model.Item;
import com.anns.appnghenhacso.R;


import java.util.ArrayList;

public class PlaynhacAdapter extends RecyclerView.Adapter<PlaynhacAdapter.ViewHolder>{

    Context context;
    ArrayList<Item> mangbaihat;

    public PlaynhacAdapter(Context context, ArrayList<Item> mangbaihat) {
        this.context = context;
        this.mangbaihat = mangbaihat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dong_play_bai_hat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item baihat = mangbaihat.get(position);
        holder.txttenbaihat.setText(baihat.getFields().getMusic_Title());
        holder.txtcasi.setText(baihat.getFields().getComposed());
        holder.txtindex.setText(position + 1 +"");
    }

    @Override
    public int getItemCount() {
        return mangbaihat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtindex,txttenbaihat,txtcasi;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtcasi = itemView.findViewById(R.id.textviewplaynhactencasi);
            txtindex = itemView.findViewById(R.id.textviewplaynhacindex);
            txttenbaihat = itemView.findViewById(R.id.textviewplaynhactenbaihat);
        }
    }
}

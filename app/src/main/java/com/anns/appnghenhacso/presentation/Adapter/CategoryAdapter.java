package com.anns.appnghenhacso.presentation.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.anns.appnghenhacso.domain.model.Model.ListMusic;
import com.anns.appnghenhacso.R;
import com.anns.appnghenhacso.presentation.Screen.Detail_Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    ArrayList<ListMusic> listMusics;
    Context context;

    public CategoryAdapter(ArrayList<ListMusic> listMusics, Context context) {
        this.listMusics = listMusics;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_category,parent,false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        holder.nameCategory.setText(listMusics.get(position).getTitleListMusic());
        Picasso.get().load(listMusics.get(position).getImageListMusic()).into(holder.image_category);
        holder.card_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,Detail_Category.class);
                intent.putExtra("ID_LIST",listMusics.get(position).getId());
                intent.putExtra("Title_LIST",listMusics.get(position).getTitleListMusic());
                intent.putExtra("Image_LIST",listMusics.get(position).getImageListMusic());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listMusics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image_category;
        TextView nameCategory;
        CardView card_category;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameCategory = itemView.findViewById(R.id.name_category);
            image_category = itemView.findViewById(R.id.image_category);
            card_category = itemView.findViewById(R.id.card_category);
        }
    }
}

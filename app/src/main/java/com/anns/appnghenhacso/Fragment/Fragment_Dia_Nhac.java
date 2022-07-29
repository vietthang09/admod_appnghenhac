package com.anns.appnghenhacso.Fragment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.anns.appnghenhacso.R;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.File;

public class Fragment_Dia_Nhac extends Fragment {

    View view;
    CircleImageView circleImageView;
    ObjectAnimator objectAnimator;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dia_nhac,container,false);
        circleImageView = view.findViewById(R.id.imagecircle);
        objectAnimator = ObjectAnimator.ofFloat(circleImageView,"rotation",0f,360f);
        objectAnimator.setDuration(10000);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.start();
        return view;
    }

    public void PlayNhac(String hinhanh) {
        if(hinhanh.contains("/storage/emulated/0/Download")){
            final BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap scaledBitmap = BitmapFactory.decodeFile(hinhanh, options);
            circleImageView.setImageBitmap(scaledBitmap);
        }else{
            Picasso.get().load(hinhanh).into(circleImageView);
        }
//        File file = new File (hinhanh);
//        if(file.exists()){
//            final BitmapFactory.Options options = new BitmapFactory.Options();
//            Bitmap scaledBitmap = BitmapFactory.decodeFile(hinhanh, options);
//            circleImageView.setImageBitmap(scaledBitmap);
//        }else{
//            Picasso.get().load(hinhanh).into(circleImageView);
//        }
    }
}

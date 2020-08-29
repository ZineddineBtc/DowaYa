package com.example.dowaya.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.dowaya.R;

public class SlideshowAdapter extends PagerAdapter {

    private Context nContext;

    public SlideshowAdapter(Context context){
        this.nContext = context;
    }

    private int[] imageSlide =
            new int[]{R.drawable.slide0, R.drawable.slide1, R.drawable.slide2};


    @Override
    public int getCount() {
        return imageSlide.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(nContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(imageSlide[position]);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}













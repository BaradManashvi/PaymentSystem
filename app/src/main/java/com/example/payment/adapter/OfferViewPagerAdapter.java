package com.example.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.payment.R;

import java.util.ArrayList;

public class OfferViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Object> offerList;

    public OfferViewPagerAdapter(Context context, ArrayList<Object> offerList) {
        this.context = context;
        this.offerList = offerList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.offer_viewpager_item,container,false);
        TextView txtOffer = view.findViewById(R.id.offerGame);
        txtOffer.setText((CharSequence) offerList.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return offerList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}

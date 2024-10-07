package com.example.payment.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.payment.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private Context context;
    private ViewPager viewPager;
    private LinearLayout lnrLyt;
    private ArrayList<Integer> offerList;
    private int count = 0;
    private Timer timer;


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void initViews(View view) {
        viewPager = view.findViewById(R.id.view_pager_home);
        lnrLyt = view.findViewById(R.id.ln_points_home);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_home,container,false);
        initViews(view);
        setupViewPager();
        View doorPayButton = view.findViewById(R.id.doorPayButton);




        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                addBottomDots(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(() ->{
                    if (count<=5){
                        viewPager.setCurrentItem(count);
                        count++;

                    }
                    else{
                        count=0;
                        viewPager.setCurrentItem(count);
                    }
                });

            }
        },500,2000);

        // Inflate the layout for this fragment
        return view;
    }
    private void setupViewPager() {
        offerList = new ArrayList<>();
        offerList.add(R.drawable.jsi);
        offerList.add(R.drawable.jse1);
        offerList.add(R.drawable.jse2);
        HomeViewPagerAdapter viewPagerAdapter = new HomeViewPagerAdapter(requireContext(), offerList);
        viewPager.setAdapter(viewPagerAdapter);
        addBottomDots(0);
    }

    private void addBottomDots(int currentPage) {
        TextView[] mTxtDot = new TextView[offerList.size()];
        lnrLyt.removeAllViews();

        for (int i = 0; i < mTxtDot.length; i++) {
            mTxtDot[i] = new TextView(requireContext()); // Use requireContext() here
            mTxtDot[i].setText(Html.fromHtml("&#8226;"));
            mTxtDot[i].setTextSize(35);
            mTxtDot[i].setTextColor(getResources().getColor(R.color.grey_400));
            lnrLyt.addView(mTxtDot[i]);
        }

        if (mTxtDot.length > 0)
            mTxtDot[currentPage].setTextColor(getResources().getColor(R.color.grey_400));
    }


    @Override
    public void onDetach() {
        super.onDetach();
        timer.cancel();
    }
}
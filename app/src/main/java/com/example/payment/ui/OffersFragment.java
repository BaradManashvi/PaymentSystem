package com.example.payment.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.payment.R;
import com.example.payment.adapter.OfferAdapter;
import com.example.payment.adapter.OfferViewPagerAdapter;
import com.example.payment.model.DealerModel;
import com.example.payment.model.OfferModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class OffersFragment extends Fragment {
    private Context context;
    private RecyclerView offerRecycler,dealersRecycler,dealersRecyclerOnLine;
    private DealerAdapter adapter;
    private ViewPager viewPager;
    private ArrayList<String> Array;
    private LinearLayout lnrLyt;
    private Timer timer;
    private int count = 0;
    private ByteArrayOutputStream offerArray;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public OffersFragment() {
        // Required empty public constructor
    }


    public static OffersFragment newInstance() {
        OffersFragment fragment = new OffersFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offers,container,false);
        initView(view);
        setUpViewPager();
        ArrayList<DealerModel> offlineMerchantList = new ArrayList<>();
        offlineMerchantList.add(new DealerModel("Starbucks","Flat","Rs."+"39","Cashback","Valid Once per User"));
        offlineMerchantList.add(new DealerModel("McDonalds","Get Burger worth","Rs."+"69","Free","For New  User"));
        offlineMerchantList.add(new DealerModel("Metro","Flat","Rs."+"19","Free","Bill payment of Rs 500"));
        offlineMerchantList.add(new DealerModel("JIO Recharge ","Cashback","Rs."+"55","Wallet","On 1st Transaction"));
        adapter = new DealerAdapter(context,offlineMerchantList);
        dealersRecycler.setAdapter(adapter);

        ArrayList<DealerModel> onlineDealerList = new ArrayList<>();
        onlineDealerList.add(new DealerModel("Zomato","Get","20%","Cashback","Valid Twice Per User"));
        onlineDealerList.add(new DealerModel("Swiggy","Get","15%","Cashback","For new user only"));
        onlineDealerList.add(new DealerModel("Sun Cinema","Get","50%","Cashback","Book 4 tickets"));
        onlineDealerList.add(new DealerModel("Practo","Get","70%","Cashback","Valid Twice Per User"));
        adapter = new DealerAdapter(context, onlineDealerList);
        dealersRecyclerOnLine.setAdapter(adapter);

        ArrayList<OfferModel>  offerlist = new ArrayList<>();
        offerlist.add(new OfferModel("Bill Payment", "25% Cashback",R.drawable.ic_bill_green));
        offerlist.add(new OfferModel("Electricity", "38% Cashback",R.drawable.ic_lightbulb_green));
        offerlist.add(new OfferModel("Water Bill", "15% Cashback",R.drawable.ic_wallet_grey));

        OfferAdapter adapter = new OfferAdapter(context,offerlist);
        offerRecycler.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);

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
                        count ++;
                    }
                    else {
                        count = 0;
                        viewPager.setCurrentItem(count);
                    }

                });
            }
        },500,2000);
        return view;
    }

    private void setUpViewPager() {
        ArrayList<Object> offerArray = new ArrayList<>();
        offerArray.add("25% Cashback");
        offerArray.add("Free Recharge");
        offerArray.add("20% off on SIB card");
        offerArray.add("10% discount Book Flight");
        OfferViewPagerAdapter viewPagerAdapter = new OfferViewPagerAdapter(context,offerArray);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(40,0,40,20);
        viewPager.setPageMargin(20);
        addBottomDots(0);


    }

    private void addBottomDots(int currentPage) {
        TextView[] mTxtDot =  new TextView[0];
        if (offerArray != null) {
            mTxtDot   = new TextView[offerArray.size()];
        }
        lnrLyt.removeAllViews();

        for (int i = 0; i < mTxtDot.length; i++) {
            mTxtDot[i] = new TextView(context);
            mTxtDot[i].setText(Html.fromHtml("&#8226;"));
            mTxtDot[i].setTextSize(35);
            mTxtDot[i].setTextColor(getResources().getColor(R.color.grey_400));
            lnrLyt.addView(mTxtDot[i]);

        }
        if (mTxtDot.length > 0)
            mTxtDot[currentPage].setTextColor(getResources().getColor(R.color.grey_400));
    }

    private void initView(View view) {
        viewPager = view.findViewById(R.id.offer_view_pager);
        lnrLyt = view.findViewById(R.id.ln_points);
        offerRecycler = view.findViewById(R.id.rv_offline_marchant);
        dealersRecycler = view.findViewById(R.id.rv_offline_marchant);
        dealersRecyclerOnLine = view.findViewById(R.id.online_dealers_recycler);
        dealersRecyclerOnLine.setNestedScrollingEnabled(false);
        dealersRecycler.setNestedScrollingEnabled(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        offerRecycler.setLayoutManager(layoutManager);
        dealersRecycler.setLayoutManager(new GridLayoutManager(context,3));
        dealersRecyclerOnLine.setLayoutManager(new GridLayoutManager(context,3));


    }

    @Override
    public void onDetach() {
        super.onDetach();
        timer.cancel();
    }
}
package com.example.payment.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payment.R;
import com.example.payment.model.DealerModel;

import java.util.ArrayList;

public class DealerAdapter extends RecyclerView.Adapter<DealerAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DealerModel> dealerModeArrayList;

    public DealerAdapter(Context context, ArrayList<DealerModel> dealerAdapterArrayList) {
        this.context = context;
        this.dealerModeArrayList = dealerModeArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dealers,parent,false);
    return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DealerAdapter.ViewHolder holder, int position) {

        holder.dealerName.setText(dealerModeArrayList.get(position).getDealer_name());

        holder.discountOffer.setText(dealerModeArrayList.get(position).getDiscount_offer());

        holder.discountAmt.setText(dealerModeArrayList.get(position).getDiscount_amt());

        holder.discountVc.setText(dealerModeArrayList.get(position).getDiscount_way());

        holder.discountDetails.setText(dealerModeArrayList.get(position).getDiscount_detail());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        private TextView dealerName,discountOffer,discountAmt,discountVc,discountDetails;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dealerName=itemView.findViewById(R.id.dealer_txt);
            discountOffer=itemView.findViewById(R.id.dealer_off);
            discountAmt=itemView.findViewById(R.id.dealer_off_price);
            discountVc=itemView.findViewById(R.id.dealer_off_bw);
            discountDetails=itemView.findViewById(R.id.discount_info);

        }
    }
}

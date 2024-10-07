package com.example.payment.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.payment.R;
import com.example.payment.model.OfferModel;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder> {
    private Context context;

    private ArrayList<OfferModel> offerModelArrayList;


    public OfferAdapter(Context context, ArrayList<OfferModel> offerModelArrayList) {
        this.context = context;
        this.offerModelArrayList = offerModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_offers,parent,false);
    return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.offerImgView.setImageResource(offerModelArrayList.get(position).getImage());
        holder.offerItem.setText(offerModelArrayList.get(position).getOffer_on());
        holder.OfferDescription.setText(offerModelArrayList.get(position).getOffer_details());

    }

    @Override
    public int getItemCount() {
        return offerModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView offerImgView;
        private TextView offerItem,OfferDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            offerImgView = itemView.findViewById(R.id.offer_img);
            offerItem = itemView.findViewById(R.id.offer_txt);
            OfferDescription = itemView.findViewById(R.id.offer_info);

        }
    }
}
